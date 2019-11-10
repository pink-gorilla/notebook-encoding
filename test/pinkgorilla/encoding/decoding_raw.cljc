(ns pinkgorilla.encoding.decoding-raw
  "
  "
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.encoding.persistence :refer [load-notebook load-str save-notebook]]
   [pinkgorilla.encoding.decode :refer [parse-notebook]]

   ))



;(def f2 "resources/diff.cljw")
(def f2 "resources/bad-format.cljw")
(deftest decode-raw-notebook
  (let [s (load-str f2)
        notebook (parse-notebook s)
       ]
    (is (= notebook nil))))

(comment
  (let [notebook (load-notebook f)]
    (count notebook)
;    (nth (:segments notebook) 0)
    ))
