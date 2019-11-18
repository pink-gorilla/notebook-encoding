(ns pinkgorilla.notebook.core
  (:require
   [clojure.string :as str]
   [pinkgorilla.notebook.uuid :refer [guuid]]
   [pinkgorilla.encoding.decode :refer [decode]]
   [pinkgorilla.encoding.encode :refer [encode-notebook]]))


(defn empty-notebook
  "creates an empty notebook"
  []
  {:ns                   nil
   :segments             {}
   :segment-order        []
   :queued-code-segments #{}
   :active-segment       nil
   :meta {}})

(defn segments-ordered [notebook]
  (let [segments (:segments notebook)
        segment-ids-ordered (:segment-order notebook)]
    (vec (map #(get segments %) segment-ids-ordered))))


(defn dissoc-in
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (assoc m k newmap))
      m)
    (dissoc m k)))


(defn dehydrate-notebook [notebook]
  (let [segments (segments-ordered notebook)
        segments-no-id (vec (map #(dissoc % :id :exception :error-text) segments))
        segments-no-id (vec (map #(dissoc-in % [:value-response :reagent]) segments-no-id))
        ]
    {:meta (:meta notebook)
     :segments segments-no-id}))

(defn to-key [segment]
  {(:id segment) segment})

(defn hydrate-notebook [notebook]
  (let [meta (:meta notebook)
        segments (:segments notebook)
        segments-with-id (vec (map #(assoc % :id (guuid) :exception nil :error-text nil) segments))
        ids (vec (map :id segments-with-id))
        m (reduce conj (map to-key segments-with-id))]
    (assoc (empty-notebook)
           :meta meta
           :segment-order ids
           :segments m)))

(defn load-notebook-hydrated [str]
  (hydrate-notebook (decode str)))

(defn save-notebook-hydrated [notebook]
  (encode-notebook (dehydrate-notebook notebook)))



(defn create-free-segment
  "creates a markdown segment"
  [content]
  {:id             (guuid)
   :type           :free
   :markup-visible false
   :content        {:value (or content "")
                    :type  "text/x-markdown"}})

(defn create-code-segment
  ([content]
   {:id               (guuid)
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


(defn insert-segment-at
  [worksheet new-index new-segment]
  (let [segment-order (:segment-order worksheet)
        segments (:segments worksheet)
        new-id (:id new-segment)
        [head tail] (split-at new-index segment-order)]
    (merge worksheet {:active-segment new-id
                      :segments       (assoc segments new-id new-segment)
                      :segment-order  (into [] (concat head (conj tail new-id)))})))


(defn remove-segment
  [worksheet seg-id]
  (let [segment-order (:segment-order worksheet)
        active-id (:active-segment worksheet)
        seg-idx (.indexOf segment-order seg-id)
        next-active-idx (if (and (= active-id seg-id) (> seg-idx 0))
                          (nth segment-order (- seg-idx 1)))
        segments (:segments worksheet)]
    (merge worksheet {:active-segment next-active-idx
                      :segments       (dissoc segments seg-id)
                      :segment-order  (into [] (remove #(= seg-id %) segment-order))})))