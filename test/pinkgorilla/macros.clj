(ns pinkgorilla.macros
  (:require [clojure.java.io :as io]))


(defmacro inline-resource
  [resource-path]
  (if (.exists (clojure.java.io/file resource-path))
    (slurp resource-path)
    (do ;(println "error: inline-resource non existing file " resource-path)
        "")) ; (clojure.java.io/resource resource-path))) 
  )
;; in cljs-file (ns markdown.core (:require-macros [useful.macros :refer [inline-resource]])) (def md-content (inline-resource "md/my-markdown-file.md")) 