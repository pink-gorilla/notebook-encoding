(ns pinkgorilla.encoding.decoding-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.encoding.default-config] ; side effects
   [pinkgorilla.encoding.protocols :refer [decode]]
   [pinkgorilla.encoding.decode :refer [parse-notebook]]
   [pinkgorilla.encoding.persistence-helper :refer [load-notebook load-str save-notebook]]))

; awb99: TODO: in cljs this currently gets an empty string and not the file

(def f2 "test/notebooks/bad-format.cljg")
(deftest decode-raw-notebook
  (let [s (load-str f2)
        parse-result (parse-notebook s)
        notebook (decode :gorilla s)]
    (is (= (type parse-result) instaparse.gll.Failure))
    (is (= notebook nil))))


