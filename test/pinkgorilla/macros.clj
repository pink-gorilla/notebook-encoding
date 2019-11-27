(ns pinkgorilla.macros
  (:require [clojure.java.io :as io]))

(defmacro inline-resource
  [resource-path]
  (if-let [res (io/resource resource-path)]
    (slurp res)
    (do
      #_(throw (Exception (str "inline-resource " resource-path " does not exist")))
      "")))
;; in cljs-file (ns markdown.core (:require-macros [useful.macros :refer [inline-resource]])) (def md-content (inline-resource "md/my-markdown-file.md")) 