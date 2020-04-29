(ns pinkgorilla.storage.core-test
  (:require
   [clojure.test :refer :all]

  ; dependencies needed to be in cljs bundle: 
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.storage.file]
   [pinkgorilla.storage.gist]
   [pinkgorilla.storage.repo]
   [pinkgorilla.storage.bitbucket]

   ;[pinkgorilla.storage.direct.file]
   ;[pinkgorilla.storage.direct.gist]
   ;[pinkgorilla.storage.direct.repo]
   ;[pinkgorilla.storage.direct.bitbucket]

   [pinkgorilla.creds :refer [creds]]))

(deftest storage-types-included
  (is (= pinkgorilla.storage.gist.StorageGist
         (type (storage/create-storage {:type :gist :id "8204fd0b2aba27f06c04dffcb4fd0a24"}))))

  (is (= pinkgorilla.storage.repo.StorageRepo
         (type (storage/create-storage {:type :repo :user "pink-gorilla"}))))

  (is (= pinkgorilla.storage.file.StorageFile
         (type (storage/create-storage {:type :file :filename "test.cljg"})))))

(deftest storage-repo-load
  (let [content "hello-repo XXXX\n"
        s (storage/create-storage {:type :repo
                                   :user "pink-gorilla"
                                   :repo "sample-notebooks"
                                   :filename "unittest-load.txt"})]

    (is (= content (storage/storage-load s nil))) ; nil creds
    (is (= content (storage/storage-load s {}))) ; empty creds
    (is (= content (storage/storage-load s (creds)))) ; full creds
    (is (= content (storage/storage-load s {:github-token ""}))) ; empty github token
    ))

(deftest storage-gist-load
  (let [content ";; gorilla-repl.fileformat = 2\n\n;; @@ [meta]\n{:test 789}\n\n;; @@\n"
        s (storage/create-storage {:type :gist
                                   :user "awb99"
                                   :id "5e5fb05046d3510745bd3285adb42715"
                                   :filename "meta1.cljg"})]

    (is (= content (storage/storage-load s nil))) ; nil creds
    (is (= content (storage/storage-load s {}))) ; empty creds
  ;  (is (= content (storage/storage-load s (creds)))) ; full creds cannot do this test because gist is user specific.
    (is (= content (storage/storage-load s {:github-token ""}))) ; empty github token
    ))




