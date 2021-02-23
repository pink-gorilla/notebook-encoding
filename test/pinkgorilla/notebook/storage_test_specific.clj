(ns pinkgorilla.notebook.storage-test-specific
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.document.default-config] ; side-effects
   [pinkgorilla.storage.protocols :as storage]
   [pinkgorilla.notebook.persistence :refer [load-notebook]]
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
           (do (->> (load-notebook store tokens)
                    (:meta)))))))