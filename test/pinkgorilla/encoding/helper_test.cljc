(ns pinkgorilla.encoding.helper-test
  (:require
   #?(:clj  [clojure.test :refer :all]
      :cljs [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.encoding.default-config] ; side effects
   [pinkgorilla.encoding.helper :refer [make-clojure-comment unmake-clojure-comment]]
   [pinkgorilla.encoding.protocols :refer [decode encode]]))

(deftest encode-comment
  (is (= ";;; #test" (make-clojure-comment "#test"))))

(deftest decode-comment
  (is (= "#test" (unmake-clojure-comment ";;; #test"))) ; simple comment with crazy character #
  (is (= "66\n88\n" (unmake-clojure-comment ";;; 66\n;;; 88\n;;; "))) ; multi line comment with last line empty
  )

