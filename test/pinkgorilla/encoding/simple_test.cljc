(ns pinkgorilla.encoding.simple-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.encoding.persistence :refer [load-notebook save-notebook]]))

(def notebook-simple
  {:version 2
   :meta {:a 17
          :msg "simple-notebook"
          :experimental :on}
   :segments
   [{:type :free
     :content {:value "#test" :type "text/x-markdown"}
     :markup-visible false}
    {:type :free
     :content {:value "" :type "text/x-markdown"} ; test for empty code segment
     :markup-visible false}
    {:type :code
     :kernel :clj
     :content {:value "(+ 7 7)"
               :type "text/x-clojure"}
     :console-response "adding numbers.."
     :value-response {:type "html" :content [:span "14"]}}
    {:type :code
     :kernel :clj
     :content {:value "" ; test for empty code segment
               :type "text/x-clojure"}
     :console-response "" ; test for empty console output
     ;:value-response {} ; test for missing value response
     }]})

(deftest encode-simple
  (let [f "/tmp/notebook-simple.cljg"
        _ (save-notebook f notebook-simple)]
    (is (= notebook-simple (load-notebook f)))))

(def notebook-meta-value
  {:version 2
   :meta {:a 17
          :msg "simple-notebook"
          :experimental :on}
   :segments
   [{:type :code
     :kernel :clj
     :content {:value "(+ 7 7)"
               :type "text/x-clojure"}
     :console-response "adding numbers.."
     :value-response {:type "html" :content ^{:p/render-as true} [:span "14"]}}]})

(deftest encode-meta-value
  (let [f "/tmp/notebook-meta-value.cljg"
        _ (save-notebook f notebook-meta-value)
        loadback (load-notebook f)]
    (is (= notebook-meta-value loadback))
    (is (= {:p/render-as true} (-> loadback :segments first :value-response :content meta)))))





