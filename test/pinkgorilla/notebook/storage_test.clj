(ns pinkgorilla.notebook.storage-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.notebook.core :as notebook]
   [pinkgorilla.storage.core-test]))




(deftest storage-with-meta
  (let [tokens nil
        store (storage/create-storage {:type :file :filename "/tmp/meta1.cljw"})
        nb (notebook/empty-notebook)
        nb (assoc nb :meta {:test 123})]
    (is (= {:test 123}
           (do (notebook/notebook-save store tokens nb)
               (-> (notebook/notebook-load store tokens)
                   (:meta)))))))