(ns pinkgorilla.storage.core-test
  (:require

 [clojure.test :refer :all]
   
  ; dependencies needed to be in cljs bundle: 
   
   [pinkgorilla.storage.storage :as storage]   
   [pinkgorilla.storage.file]
   [pinkgorilla.storage.gist]
   [pinkgorilla.storage.repo]
   [pinkgorilla.storage.bitbucket]))

(deftest gist-storage
  (is (= pinkgorilla.storage.gist.StorageGist 
         (type (storage/create-storage {:type :gist :id "8204fd0b2aba27f06c04dffcb4fd0a24"}))))
  
  (is (= pinkgorilla.storage.repo.StorageRepo
         (type (storage/create-storage {:type :repo :user "pink-gorilla"}))))
  
(is (= pinkgorilla.storage.file.StorageFile
       (type (storage/create-storage {:type :file :filename "test.cljg"}))))
  
  )

