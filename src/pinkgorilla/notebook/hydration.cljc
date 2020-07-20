(ns pinkgorilla.notebook.hydration
  (:require
   #?(:clj [taoensso.timbre :refer [info error]]
      :cljs [taoensso.timbre :refer-macros [info error]])
   [pinkgorilla.notebook.uuid :refer [id]]
   [pinkgorilla.encoding.protocols :refer [decode encode]]
   [pinkgorilla.storage.protocols :refer [determine-encoding storage-load storage-save]]))

;; helper functions

(defn- assoc-when [r key val]
  (if val
    (assoc r key val)
    r))

(defn- process-type [segment type fun]
  (if (= type (:type segment))
    (fun segment)
    segment))

(defn- dissoc-in
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (assoc m k newmap))
      m)
    (dissoc m k)))

 ;; hydration (load persisted notebook)

(defn hydrate-md [s]
  (let [md (get-in s [:content :value])]
    {:type :md
     :md md}))

(defn hydrate-code [s]
  (let [code (get-in s [:content :value])
        picasso-spec (get-in s [:value-response])
        out (get-in s [:console-response])]
    (-> s
        (dissoc :content)
        (assoc-when :code code)

        (dissoc :value-response)
        (assoc-when :picasso picasso-spec)

        (dissoc :console-response)
        (assoc-when :out out))))

(defn- hydrate-segment [segment]
  (-> segment
      (process-type :code hydrate-code)
      (process-type :free hydrate-md)
      (assoc :id (id))))

(defn- to-key [segment]
  {(:id segment) segment})

(defn hydrate [notebook]
  (let [segments (:segments notebook)
        segments-hydrated (vec (map hydrate-segment segments))]
    (assoc {:active    nil ; active segment
            :ns        nil  ; current namespace
            :queued   #{}} ; code segments that are qued for evaluation
           :meta (:meta notebook)
           :segments (reduce conj (map to-key segments-hydrated))
           :order (vec (map :id segments-hydrated)))))

(defn load-notebook [storage tokens]
  (if-let [encoding-type (determine-encoding storage)]
    (->> (storage-load storage tokens)
         (decode encoding-type)
         hydrate)
    (do
      (error "cannot load notebook - format cannot be determined! " storage)
      nil)))

; dehydrate / save

(defn segments-ordered [notebook]
  (let [segments (:segments notebook)
        segment-ids-ordered (:order notebook)]
    (vec (map #(get segments %) segment-ids-ordered))))

(defn dehydrate-md [s]
  ;(info "dehydrating md segment: " s)
  (let [md (get-in s [:md])]
    {:type :free
     :markup-visible false
     :content {:type "text/x-markdown"
               :value md}}))

(defn dehydrate-code [segment]
  ;(info "dehydrating code segment: " s)
  (let [{:keys [kernel code picasso out]} segment]
    ; #(dissoc-in % [:value-response :reagent])
    ; #(dissoc % :id :exception :error-text)
    (-> {:type :code
         :kernel kernel
         :content {:type "text/x-clojure"
                   :value code}}
        (assoc-when :value-response picasso)
        (assoc-when :console-response out))))

(defn dehydrate-segment [segment]
  (-> segment
      (process-type :code dehydrate-code)
      (process-type :md dehydrate-md)
      (dissoc :id)))

(defn dehydrate [notebook]
  (let [segments (segments-ordered notebook)
        ;_ (info "segments ordered: " segments)
        segments-dehydrated (vec (map dehydrate-segment segments))
        notebook-dehydrated {;:version (:version notebook)
                             :meta (:meta notebook)
                             :segments segments-dehydrated}]
    ;(info "dehydrated: " notebook-dehydrated)
    notebook-dehydrated))

(defn tap [note x]
  (info note x)
  x)

(defn save-nb [storage tokens nb]
  (storage-save storage nb tokens))

(defn save-notebook [storage tokens notebook]
  (if-let [format (determine-encoding storage)]
    (do (info "saving notebook with format: " format)
        (->> notebook
             (dehydrate)
             ;(tap "dehydrated nb:")
             (encode format)
             ;(tap "encoded nb: ")
             (save-nb storage tokens))
        {:success "notebook saved!"})
    (do (error "could not save notebook, because encoding cannot be determined: " storage)
        {:error "could not determine storage-format!"})))


;; manipulate hydrated notebook


(defn create-free-segment
  "creates a markdown segment"
  [content]
  {:id             (id)
   :type           :free
   :markup-visible false
   :content        {:value (or content "")
                    :type  "text/x-markdown"}})

(defn create-code-segment
  ([content]
   {:id               (id)
    :type             :code
    :kernel           :clj
    :content          {:value (or content "")
                       :type  "text/x-clojure"}
    :console-response ""
    :value-response   {:type "html" :value [:span]}
    :error-text       nil
    :exception        nil}))

(defn to-code-segment
  [free-segment]
  {:id               (:id free-segment)
   :type             :code
   :kernel           :clj
   :content          {:value (get-in free-segment [:content :value])
                      :type  "text/x-clojure"}
   :console-response nil
   :value-response   nil
   :error-text       nil
   :exception        nil})

(defn to-free-segment
  [code-segment]
  {:id             (:id code-segment)
   :type           :free
   :markup-visible false
   :content        {:value (get-in code-segment [:content :value])
                    :type  "text/x-markdown"}})

(defn- update-segment
  [fun notebook seg-id]
  (let [segment (get-in notebook [:segments seg-id])
        segment-new (fun segment)]
    (assoc-in notebook [:segments seg-id] segment-new)))

(defn- update-segment-active
  [notebook fun]
  (let [seg-id (:active notebook)]
    (update-segment fun notebook seg-id)))

(defn- update-segment-all
  [notebook fun]
  (reduce (partial update-segment fun) notebook (:order notebook)))

(defn- clear-output [segment]
  (dissoc segment
          :out
          :picasso
          :value
          :err
          :ex
          :root-ex))

(defn clear-active
  [notebook]
  (update-segment-active notebook clear-output))

(defn clear-all
  [notebook]
  (update-segment-all notebook clear-output))

(defn insert-segment-at
  [notebook new-index new-segment]
  (let [{:keys [order active segments]} notebook
        new-id (:id new-segment)
        [head tail] (split-at new-index order)]
    (merge notebook {:active new-id
                     :segments       (assoc segments new-id new-segment)
                     :order  (into [] (concat head (conj tail new-id)))})))

(defn remove-segment
  [notebook seg-id]
  (let [{:keys [order active segments]} notebook
        seg-idx (.indexOf order seg-id)
        next-active-idx (if (and (= active seg-id) (> seg-idx 0))
                          (nth order (- seg-idx 1)))]
    (merge notebook {:active next-active-idx
                     :segments       (dissoc segments seg-id)
                     :order  (into [] (remove #(= seg-id %) order))})))

(comment

  (save-notebook
   (pinkgorilla.storage.protocols/create-storage
    {:type :file :filename "/tmp/demo.cljg"})
   {}
   (hydrate (pinkgorilla.notebook.template/new-notebook)))



 ; 
  )


