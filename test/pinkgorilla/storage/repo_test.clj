(ns pinkgorilla.storage.repo-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.storage.github :refer [load-repo save-repo]]))

(deftest repo-load-nil-token
  (let [token nil
        content (str "hello-repo XXXX\n")]
    (is (= content
           (load-repo "pink-gorilla" "unittest-notebooks" "unittest-load.txt" token)))))

(deftest repo-load-empty-token
  (let [token ""
        content (str "hello-repo XXXX\n")]
    (is (= content
           (load-repo "pink-gorilla" "unittest-notebooks" "unittest-load.txt" token)))))

(deftest repo-load
  (let [token (:github-token (creds))
        content (str "hello-repo XXXX\n")]
    (is (= content
           (load-repo "pink-gorilla" "unittest-notebooks" "unittest-load.txt" token)))))

#_(deftest repo-load-bad
    (let [token (:github-token creds)
          content (str "hello-repo XXXX\n")]
      (is (= content
             (load-repo "pink-gorilla" "unittest-notebooks" "samples/uiplugin/gorillaplot/central-limit.cljg" token)))))

(defn tap [s]
  (println "repo: " s)
  s)

(deftest repo-storage
  (let [token (:github-token (creds))
        content (str "hello-repo " (rand-int 10000))]
    (is (= content
           (do (save-repo "pink-gorilla" "unittest-notebooks" "unittest.txt" content token)
               (-> (load-repo "pink-gorilla" "unittest-notebooks" "unittest.txt" token)
                   ;(tap)
                   ))))))

