(ns pinkgorilla.encoding.a
  (:require
   ;[clj.test :refer-macros [async deftest is testing]]
   [clojure.test :refer :all]
   [clojure.string :as str]
   [cognitect.transit :as t]
   [pinkgorilla.encoding.decode :refer [decode]]
   [pinkgorilla.encoding.encode :refer [encode-notebook]])
  (:import
   [java.io ByteArrayInputStream ByteArrayOutputStream]))



(defn load-notebook [f]
  (let [s (slurp f)]
    (decode s)))

(defn save-notebook [f notebook]
  (let [s (encode-notebook notebook)]
    (spit f s)))


(def notebook-simple
  {:segments
   [{:type :free 
     :content {:value "#test" :type "text/x-markdown"} 
     :markup-visible false}
    {:type :code 
     :kernel :clj 
     :content {:value "(+ 7 7)"
               :type "text/x-clojure"} 
     :console-response "adding numbers.." 
     :value-response {:type "html" :content [:span "14"]}}
    ]})

(deftest encode-simple
  (let [f "/tmp/notebook-simple.cljw"
        _ (save-notebook f notebook-simple)]
    (is (= notebook-simple (load-notebook f)))))


  ; (def nb (slurp "resources/notebook1.cljw"))
;(def nb (slurp "resources/demo.cljw"))
(def f "resources/reagent-manipulate.clj")


#_(deftest reload-existing-notebook
  (let [notebook (load-notebook f)
        f-out "/tmp/notebook-unittest.cljw"
        _ (save-notebook f-out notebook)
        notebook-reloaded (load-notebook f-out)]
    (is (= notebook notebook-reloaded))))


