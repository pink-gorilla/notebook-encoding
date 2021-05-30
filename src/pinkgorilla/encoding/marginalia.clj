(ns pinkgorilla.encoding.marginalia
  (:require
   [clojure.string :as str]
   [marginalia.parser :refer [parse #_comments-enabled? *comments-enabled*]]
   [notebook.core :refer [new-notebook set-meta-key add-segments md-segment code-segment]]
   [pinkgorilla.encoding.protocols :refer [decode]])
  ;(:import [java.io #_PushbackReader])
  )

(defn marginalia->segment
  [{:keys [raw type]}] ; docstring form
  ;(println "marginalia->segment " all)
  (case type
    :code (code-segment :clj (str raw))
    :comment (if (str/starts-with? raw "=>")
               nil ;(str "Result:" (code-block raw))
               (md-segment raw))
    nil))

(defmethod decode :marginalia [_ source-str]
  ;(println "decoding marginalia format.. " source-str)
  (let [notebook (-> (new-notebook)
                     (set-meta-key :tagline "imported from clj")
                     (set-meta-key :tags "marginalia"))
        forms (binding [*comments-enabled* (atom true)]
                (parse source-str))
        segments (map marginalia->segment forms)
        segments (remove nil? segments)]
    (add-segments notebook segments)))


