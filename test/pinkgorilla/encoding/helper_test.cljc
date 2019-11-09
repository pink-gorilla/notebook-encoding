(ns pinkgorilla.encoding.helper-test
  (:require
   #?(:clj  [clojure.test :refer :all]
      :cljs [cljs.test :refer-macros [async deftest is testing]])

   [pinkgorilla.encoding.helper :refer [make-clojure-comment unmake-clojure-comment]]
   [pinkgorilla.encoding.decode :refer [decode]]
   [pinkgorilla.encoding.encode :refer [encode-notebook]]))


(deftest encode-comment
  (is (= ";;; #test" (make-clojure-comment "#test"))))


(deftest decode-comment
  (is (= "#test" (unmake-clojure-comment ";;; #test"))))

