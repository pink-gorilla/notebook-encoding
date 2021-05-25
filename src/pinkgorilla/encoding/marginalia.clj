(ns pinkgorilla.encoding.marginalia
  (:require
   [clojure.string :as str]
   [marginalia.parser :refer [parse #_comments-enabled? *comments-enabled*]]
   [pinkgorilla.notebook.core :refer [empty-notebook assoc-meta add-segments md->segment code->segment]]
   [pinkgorilla.encoding.protocols :refer [decode]])
  ;(:import [java.io #_PushbackReader])
  )

(defn marginalia->segment
  [{:keys [raw type]}] ; docstring form
  ;(println "marginalia->segment " all)
  (case type
    :code (code->segment :clj (str raw))
    :comment (if (str/starts-with? raw "=>")
               nil ;(str "Result:" (code-block raw))
               (md->segment raw))
    nil))

(defmethod decode :marginalia [_ source-str]
  ;(println "decoding marginalia format.. " source-str)
  (let [notebook (-> (empty-notebook)
                     (assoc-meta :tagline "imported from clj")
                     (assoc-meta :tags "marginalia"))
        forms (binding [*comments-enabled* (atom true)]
                (parse source-str))
        segments (map marginalia->segment forms)
        segments (remove nil? segments)]
    (add-segments notebook segments)))


