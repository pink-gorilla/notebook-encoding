(ns pinkgorilla.notebook.storage-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.core :refer [empty-notebook]]
   [pinkgorilla.notebook.hydration :refer [notebook-load notebook-save]]
   [pinkgorilla.storage.core-test]
   [pinkgorilla.creds :refer [creds]]))

(deftest file-storage-with-meta
  (let [tokens nil
        store (create-storage {:type :file
                               :filename "/tmp/meta1.cljg"})
        meta {:test 123}
        nb empty-notebook
        nb (assoc nb :meta meta)]
    (is (= meta
           (do (notebook-save store tokens nb)
               (-> (notebook-load store tokens)
                   (:meta)))))))

(deftest gist-storage-with-meta
  (let [tokens (creds)
        id (:gist-id tokens)
        store (create-storage {:type :gist
                               :filename "meta1.cljg"
                               :id id
                               :description "unittest-meta1"})
        meta {:test 789}
        nb empty-notebook
        nb (assoc nb :meta meta)]
    (is (= meta
           (do (notebook-save store tokens nb)
               (-> (notebook-load store tokens)
                   (:meta)))))))

(deftest gist-storage-with-meta-no-creds
  (let [tokens {}
        id (:gist-id (creds))
        store (create-storage {:type :gist
                               :filename "meta1.cljg"
                               :id id
                               :description "unittest-meta1"})
        meta {:test 789}
        nb empty-notebook
        nb (assoc nb :meta meta)]
    (is (= meta
           (do (notebook-save store tokens nb)
               (-> (notebook-load store tokens)
                   (:meta)))))))

(deftest repo-storage-with-meta
  (let [tokens (creds)
        store (create-storage {:type :repo
                               :user "pink-gorilla"
                               :repo "unittest-notebooks"
                               :filename "unittest-meta1.cljg"})
        meta {:test 456}
        nb empty-notebook
        nb (assoc nb :meta meta)]
    (is (= meta
           (do (notebook-save store tokens nb)
               (->> (notebook-load store tokens)
                    (:meta)))))))

