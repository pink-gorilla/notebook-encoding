(ns pinkgorilla.storage.gist-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.storage.github :refer [load-gist save-gist]]))

(defn tap [s]
  (println "gist id: " s)
  s)

(deftest gist-storage
  (let [token (:github-token creds)
        id (:gist-id creds)
        content (str "hello-" (rand-int 10000))
        filename "raw-str.txt"]
    (is (= content
           (do (save-gist id "unittest storage-raw" true filename content token)  ; id=nill = create
               (load-gist id filename token ))))))

