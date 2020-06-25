(ns pinkgorilla.encoding.marginalia
  (:require
   [clojure.string :as str]
   [marginalia.parser :refer [parse comments-enabled? *comments-enabled*]]
   [pinkgorilla.notebook.core :refer [empty-notebook assoc-meta add-segments md->segment code->segment]]
   [pinkgorilla.encoding.protocols :refer [decode]])
  (:import [java.io PushbackReader]))

(defn marginalia->segment
  [{:keys [docstring raw form type] :as all}]
  ;(println "marginalia->segment " all)
  (case type
    :code (code->segment :clj (str raw))
    :comment (if (str/starts-with? raw "=>")
               nil ;(str "Result:" (code-block raw))
               (md->segment raw))
    nil))

(defmethod decode :marginalia [_ source-str]
  ;(println "decoding marginalia format.. " source-str)
  (let [notebook (assoc-meta empty-notebook :msg "imported from clj")
        forms (binding [*comments-enabled* (atom true)]
                (parse source-str))
        segments (map marginalia->segment forms)
        segments (remove nil? segments)]
    (add-segments notebook segments)))

