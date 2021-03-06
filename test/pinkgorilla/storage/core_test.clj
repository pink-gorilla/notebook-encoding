(ns pinkgorilla.storage.core-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.storage.protocols :as storage]
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.storage.settings :refer [storage-data]]))

(deftest storage-types-included
  (is (= pinkgorilla.storage.gist.StorageGist
         (type (storage/create-storage {:type :gist :id "8204fd0b2aba27f06c04dffcb4fd0a24"}))))

  (is (= pinkgorilla.storage.repo.StorageRepo
         (type (storage/create-storage {:type :repo :user "pink-gorilla"}))))

  (is (= pinkgorilla.storage.file.StorageFile
         (type (storage/create-storage {:type :file :filename "test.cljg"})))))

(deftest storage-repo-load
  (let [content "hello-repo XXXX\n"
        s (storage/create-storage (:core storage-data))]
    (is (= content (storage/storage-load s nil))) ; nil creds
    (is (= content (storage/storage-load s {}))) ; empty creds
    (is (= content (storage/storage-load s (creds)))) ; full creds
    (is (= content (storage/storage-load s {:github-token ""}))) ; empty github token
    ))

(deftest storage-gist-load
  (let [content ";; gorilla-repl.fileformat = 2\n\n;; @@ [meta]\n{:test 789}\n\n;; @@\n"
        s (storage/create-storage (:gist-load storage-data))]

    (is (= content (storage/storage-load s nil))) ; nil creds
    (is (= content (storage/storage-load s {}))) ; empty creds
  ;  (is (= content (storage/storage-load s (creds)))) ; full creds cannot do this test because gist is user specific.
    (is (= content (storage/storage-load s {:github-token ""}))) ; empty github token
    ))




