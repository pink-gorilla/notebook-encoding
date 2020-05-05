(ns pinkgorilla.notebook.storage-test-specific
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.notebook.core :as notebook]
   [pinkgorilla.storage.core-test]
   [pinkgorilla.creds :refer [creds]]))

;;version 1 does not have meta information


(deftest repo-storage-version-1
  (let [tokens (creds)
        store (storage/create-storage {:type :repo
                                       :user "pink-gorilla"
                                       :repo "unittest-notebooks"
                                       :filename "samples/v1-no-meta.cljg"})
        meta {}]
    (is (= meta
           (do (->> (notebook/notebook-load store tokens)
                    (:meta)))))))