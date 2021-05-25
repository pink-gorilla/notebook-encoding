(ns pinkgorilla.storage.repo-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.encoding.default-config] ; side effects
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.storage.impl.repo :refer [load-repo save-repo]]
   [pinkgorilla.storage.settings :refer [storage-data]]))

(defn load-repo2 [id token]
  (let [args (conj (id storage-data) token)]
    (println "load-repo " id)
    (apply load-repo args)))

(defn save-repo2 [id content token]
  (let [args (conj (id storage-data) content token)]
    (println "save-repo " args)
    (apply save-repo args)))

(deftest repo-load-nil-token
  (let [token nil
        content (str "hello-repo XXXX\n")]
    (is (= content
           (load-repo2 :load token)))))

(deftest repo-load-empty-token-creds
  (let [token ""
        content (str "hello-repo XXXX\n")]
    (is (= content
           (load-repo2 :load token)))))

(deftest repo-load-creds
  (let [tokens (creds)
        content (str "hello-repo XXXX\n")]
    (is (= content
           (load-repo2 :load tokens)))))

#_(deftest repo-load-bad
    (let [tokens (creds)
          content (str "hello-repo XXXX\n")]
      (is (= content
             (load-repo "pink-junkjard" "unittest-notebooks" "samples/uiplugin/gorillaplot/central-limit.cljg" tokens)))))

(defn tap [s]
  (println "repo: " s)
  s)

(deftest repo-storage-creds
  (let [tokens (creds)
        content (str "hello-repo " (rand-int 10000))]
    (is (= content
           (do (save-repo2 :write content tokens)
               (-> (load-repo2 :write tokens)
                   ;(tap)
                   ))))))

