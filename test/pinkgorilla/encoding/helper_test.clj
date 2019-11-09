(ns pinkgorilla.encoding.helper-test
  (:require
   ;[clj.test :refer-macros [async deftest is testing]]
   [clojure.test :refer :all]
   [clojure.string :as str]
   [cognitect.transit :as t]
   [pinkgorilla.encoding.helper :refer [make-clojure-comment unmake-clojure-comment]]
   [pinkgorilla.encoding.decode :refer [decode]]
   [pinkgorilla.encoding.encode :refer [encode-notebook]])
  (:import
   [java.io ByteArrayInputStream ByteArrayOutputStream]))


(deftest encode-comment
    (is (= ";;; #test" (make-clojure-comment "#test"))))


(deftest decode-comment
  (is (= "#test" (unmake-clojure-comment ";;; #test"))))

