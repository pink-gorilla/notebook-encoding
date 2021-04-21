(ns pinkgorilla.storage.gist-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.storage.impl.github :refer [load-gist save-gist]]))

(defn tap [s]
  (println "gist id: " s)
  s)

(deftest gist-storage-creds
  (let [tokens (creds)
        gist-id (:gist-id tokens)
        content (str "hello-" (rand-int 10000))
        filename "raw-str.txt"]
    (is (= content
           (do (save-gist gist-id "unittest storage-raw" true filename content tokens) ; id=nill = create
               (load-gist gist-id filename tokens))))))

