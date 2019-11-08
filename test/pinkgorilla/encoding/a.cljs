(ns pinkgorilla.encoding.a
  (:require
   [cljs.test :refer-macros [async deftest is testing]]
   [instaparse.core :as insta]
   [pinkgorilla.encoding.decode :refer [decode]]))


;; DUMMY TEST

(defn add [x y] (+ x y))

(deftest add-x-to-y-a-few-times
  (is (= 5 (add 2 3)))
  (is (= 5 (add 1 4)))
  (is (= 5 (add 3 2))))


;; Instaparse test


(def as-and-bs
  (insta/parser
   "S = AB*
     AB = A B
     A = 'a'+
     B = 'b'+"))

(deftest instaparse-working
  (is (= (as-and-bs "aaaaabbbaaaabb") {})))



;

(def token
  (insta/parser
   "S = AB*
     AB = A B
     A = 'a'+
     <B> = #'[a-zA-Z]'  " ))


(deftest instaparse-token
  (is (= (token "aAbaaaabA") {})))
   
   
   

(deftest decode-working
  (is (= (decode "") {})))


