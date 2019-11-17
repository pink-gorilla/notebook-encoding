(ns pinkgorilla.storage.gist-test
  (:require 
   [clojure.test :refer :all]
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.storage.github :refer [load-gist save-gist]]))
  
  
  
  (defn tap [s]
    (println "gist id: " s)
    s)

(deftest gist-storage
  (is (= "hello!" 
         (-> (save-gist nil "test" true "test.txt" "hello!" (:github creds)) ; id=nill = create
             (tap)
             (:id)
             (load-gist (:github creds))
             (:test.txt)
             ))))

