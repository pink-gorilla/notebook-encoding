(ns pinkgorilla.encoding.decode 
  (:require [instaparse.core :as insta]))





(defn decode [s]
  {:meta {:format 3
          :author "awb99"
          :id "abcd-1234-5678"
          :date "2019-11-08 09:40:00Z"
          :tags [:super :vega :ui :sample]}
   :widget-state  {:a 1
                   :b "2"}
   :segments [{:type :free :input "#hello"}
              {:type :code :kernel :clj :input "(+ 1 2)" :output "3"}
              {:type :code :kernel :cljs :input "(+ 1 2)" :output "3"}
              {:type :code :kernel :mock :input "(+ 1 2)" :output "Everything is wunderbar"}]})