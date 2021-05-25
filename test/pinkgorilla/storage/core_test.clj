(ns pinkgorilla.storage.core-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.encoding.default-config] ; side effects
   [pinkgorilla.storage.protocols :as storage :refer [storage->map]]
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.storage.settings :refer [storage-data]]))

(defn roundtrip [m]
  (let [s (storage/create-storage m)]
    (storage->map s)))

(deftest to-map
  (let [res {:type :res :filename "bongo.cljg"}
        f {:type :file :filename "bongo.cljg"}]
    (is (= res (roundtrip res)))
    (is (= f (roundtrip f)))
    ;
    ))

(deftest storage-types-included
  (is (= pinkgorilla.storage.gist.StorageGist
         (type (storage/create-storage {:type :gist :id "8204fd0b2aba27f06c04dffcb4fd0a24"}))))

  (is (= pinkgorilla.storage.repo.StorageRepo
         (type (storage/create-storage {:type :repo :user "pink-gorilla"}))))

  (is (= pinkgorilla.storage.file.StorageFile
         (type (storage/create-storage {:type :file :filename "test.cljg"}))))

  (is (= pinkgorilla.storage.res.StorageRes
         (type (storage/create-storage {:type :res :filename "test.cljg"})))))

(deftest storage-res-load
  (let [s (storage/create-storage     {:type :res :filename "bongo.cljg"})
        s-nil (storage/create-storage {:type :res :filename "bongo-not-existing.cljg"})]
    (is (= "{:bongo 1}" (storage/storage-load s nil)))
    (is (= nil (storage/storage-load s-nil nil)))))

(deftest storage-repo-load
  (let [content "hello-repo XXXX\n"
        s (storage/create-storage (:core storage-data))]
    (is (= content (storage/storage-load s nil))) ; nil creds
    (is (= content (storage/storage-load s {}))) ; empty creds
    (is (= content (storage/storage-load s (creds)))) ; full creds
    ))

(deftest storage-gist-load
  (let [content ";; gorilla-repl.fileformat = 2\n\n;; @@ [meta]\n{:test 789, :id 99}\n\n;; @@\n"
        s (storage/create-storage (:gist-load storage-data))]

    (is (= content (storage/storage-load s nil))) ; nil creds
    (is (= content (storage/storage-load s {}))) ; empty creds
  ;  (is (= content (storage/storage-load s (creds)))) ; full creds cannot do this test because gist is user specific.
    ))




