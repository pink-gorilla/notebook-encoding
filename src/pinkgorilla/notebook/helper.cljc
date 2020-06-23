(ns pinkgorilla.notebook.helper)

(def empty-notebook
  {:version 2
   :meta {}
   :segments []})

(defn assoc-meta
  [notebook tag value]
  (assoc-in notebook [:meta tag] value))

(defn md->segment [md]
  {:type :free
   :content {:value md
             :type "text/x-markdown"}
   :markup-visible false})

(defn code->segment [kernel code]
  {:type :code
   :kernel kernel
   :content  {:value code
              :type "text/x-clojure"}})

(defn add-segments [notebook segments]
  (let [segments (into []
                       (concat (:segments notebook) segments))]
    (assoc notebook :segments segments)))