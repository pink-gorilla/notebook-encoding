(ns pinkgorilla.bongo
  (:require
  [cljs.test :refer-macros [async deftest is testing]]))


;; DUMMY TEST

(defn add [x y] (+ x y))

(deftest add-x-to-y-a-few-times
  (is (= 5 (add 2 3)))
  (is (= 5 (add 1 4)))
  (is (= 5 (add 3 2))))



