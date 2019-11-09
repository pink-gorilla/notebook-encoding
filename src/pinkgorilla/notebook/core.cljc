(ns pinkgorilla.notebook.core
  (:require
   [clojure.string :as str]
   [pinkgorilla.notebook.uuid :refer [uuid]]))


(defn empty-notebook 
  "creates an empty notebook" 
  []
  {:ns                   nil
   :segments             {}
   :segment-order        []
   :queued-code-segments #{}
   :active-segment       nil})


(defn create-free-segment
  "creates a markdown segment"
  [content]
  {:id             (uuid)
   :type           :free
   :markup-visible false
   :content        {:value (or content "")
                    :type  "text/x-markdown"}})

(defn create-code-segment
  ([content]
     {:id               (uuid)
      :type             :code
      :kernel           :clj                        ;; default-cljs
      :content          {:value (or content "")
                         :type  "text/x-clojure"}
      :console-response nil
      :value-response   nil
      :error-text       nil
      :exception        nil}))

(defn to-code-segment
  [free-segment]
  {:id               (:id free-segment)
   :type             :code
   ;; TODO forcing :default-clj is not so nice
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