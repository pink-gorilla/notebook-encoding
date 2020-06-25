(ns pinkgorilla.storage.repo-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.storage.github :refer [load-repo save-repo]]))

(def data
  {:load ["pink-gorilla" "unittest-notebooks" "unittest-load.txt"]
   :write ["pink-gorilla" "unittest-notebooks" "unittest-dynamic.txt"]})

(defn load-repo2 [id token]
  (let [args (conj (id data) token)]
    (println "load-repo " id)
    (apply load-repo args)))

(defn save-repo2 [id content token]
  (let [args (conj (id data) content token)]
    (println "save-repo " args)
    (apply save-repo args)))

(deftest repo-load-nil-token
  (let [token nil
        content (str "hello-repo XXXX\n")]
    (is (= content
           (load-repo2 :load token)))))

(deftest repo-load-empty-token
  (let [token ""
        content (str "hello-repo XXXX\n")]
    (is (= content
           (load-repo2 :load token)))))

(deftest repo-load
  (let [token (:github-token (creds))
        content (str "hello-repo XXXX\n")]
    (is (= content
           (load-repo2 :load token)))))

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
           (do (save-repo2 :write content token)
               (-> (load-repo2 :write token)
                   ;(tap)
                   ))))))

