(ns pinkgorilla.encoding.simple
  (:require
    #?(:clj [clojure.test :refer :all]
       :cljs  [cljs.test :refer-macros [async deftest is testing]])
      [pinkgorilla.encoding.persistence :refer [load-notebook save-notebook]]
      )
)


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


