(ns pinkgorilla.notebook.core
  (:require
   [pinkgorilla.notebook.uuid :refer [guuid]]))

(defn empty-notebook []
  {:meta {:id (guuid)}
   :segments []})

(defn assoc-meta
  [notebook tag value]
  (assoc-in notebook [:meta tag] value))

(defn md->segment [md]
  {:type :md
   :data (or md "")})

(defn code->segment [kernel code]
  {:type :code
   :data {:kernel (or kernel :clj)
          :code (or code "")}
   :state {}})

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

