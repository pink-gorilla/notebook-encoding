(ns pinkgorilla.notebook.core)

(def empty-notebook
  {:meta {}
   :segments []})

(defn assoc-meta
  [notebook tag value]
  (assoc-in notebook [:meta tag] value))

(defn md->segment [md]
  {:type :free
   :markup-visible false
   :content {:value (or md "")
             :type "text/x-markdown"}})

(defn code->segment [kernel code]
  {:type :code
   :kernel (or kernel :clj)
   :content  {:value (or code "")
              :type "text/x-clojure"}})

(defn add-segments [notebook segments]
  (let [segments (into []
                       (concat (:segments notebook) segments))]
    (assoc notebook :segments segments)))

(defn add-segment [notebook segment]
  (add-segments notebook [segment]))

(defn add-md [notebook & md-args]
  (let [md (apply str md-args)]
    (add-segment notebook (md->segment md))))

(defn add-code [notebook kernel & code-args]
  (let [code (apply str code-args)]
    (add-segment notebook (code->segment kernel code))))

