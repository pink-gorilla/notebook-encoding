(ns pinkgorilla.storage.repo-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.storage.github :refer [load-repo save-repo]]))



(deftest repo-load
  (is (= "test!\n"
         (:content (load-repo "pink-gorilla" "sample-notebooks" "/test.txt" (:github creds))))))


(defn tap [s]
  (println "repo: " s)
  s)


(deftest gist-storage
  (let [token (:github creds)
        content (str (rand-int 10000))]
    (is (= content
           (do (save-repo "pink-gorilla" "sample-notebooks" "unittest.txt" content token)
               (-> (load-repo "pink-gorilla" "sample-notebooks" "unittest.txt" token)
                   ;(tap)
                   (:content)))))))

