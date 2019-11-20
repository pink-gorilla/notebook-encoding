(ns pinkgorilla.notebook.storage-test-specific
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.notebook.core :as notebook]
   [pinkgorilla.storage.core-test]
  [pinkgorilla.creds :refer [creds]]
   ))



;;version 1 does not have meta information
;; TODO: deas tell difference in file format

(deftest repo-storage-version-1
  (let [tokens (creds)
        store (storage/create-storage {:type :repo 
                                       :user "pink-gorilla" 
                                       :repo "sample-notebooks" 
                                       :filename "samples/uiplugin/gorillaplot/central-limit.cljg"})
        meta {}]
    (is (= meta
           (do (->> (notebook/notebook-load store tokens)
                   (:meta)))))))