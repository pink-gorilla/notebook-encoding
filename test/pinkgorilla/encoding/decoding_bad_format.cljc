(ns pinkgorilla.encoding.decoding-bad-format
  "
  "
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.encoding.persistence :refer [load-notebook load-str save-notebook]]
   [pinkgorilla.encoding.decode :refer [parse-notebook decode]]))

; awb99: TODO: in cljs this currently gets an empty string and not the file

(def f2 "resources/bad-format.cljw")
(deftest decode-raw-notebook
  (let [s (load-str f2)
        parse-result (parse-notebook s)
        notebook (decode s)]
    (is (= (type parse-result) instaparse.gll.Failure))
    (is (= notebook nil))))


