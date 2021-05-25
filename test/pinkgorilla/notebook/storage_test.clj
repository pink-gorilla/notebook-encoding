(ns pinkgorilla.notebook.storage-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.encoding.default-config] ; side effects
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.core :refer [empty-notebook]]
   [pinkgorilla.notebook.persistence :refer [load-notebook save-notebook]]
   [pinkgorilla.storage.core-test]
   [pinkgorilla.creds :refer [creds]]))

(defn nb-no-seg-id [nb]
  (let [segs (:segments nb)
        segs-no-id (into []
                         (map #(dissoc % :id) segs))]
    (assoc nb :segments segs-no-id)))

(deftest file-storage-with-meta
  (let [tokens nil
        store (create-storage {:type :file
                               :filename "/tmp/meta1.cljg"})
        meta {:test 123 :id 77}
        nb empty-notebook
        nb (assoc nb :meta meta)]
    (is (= meta
           (do (save-notebook store tokens nb)
               (-> (load-notebook store tokens)
                   nb-no-seg-id
                   (:meta)))))))

(deftest gist-storage-with-meta
  (let [tokens (creds)
        id (:gist-id tokens)
        store (create-storage {:type :gist
                               :filename "meta1.cljg"
                               :id id
                               :description "unittest-meta1"})
        meta {:test 789 :id 99}
        nb empty-notebook
        nb (assoc nb :meta meta)]
    (is (= meta
           (do (save-notebook store tokens nb)
               (-> (load-notebook store tokens)
                   nb-no-seg-id
                   (:meta)))))))

(deftest gist-storage-with-meta-no-creds
  (let [tokens {}
        id (:gist-id (creds))
        store (create-storage {:type :gist
                               :filename "meta1.cljg"
                               :id id
                               :description "unittest-meta1"})
        meta {:test 789 :id 99}
        nb empty-notebook
        nb (assoc nb :meta meta)]
    (is (= meta
           (do (save-notebook store tokens nb)
               (-> (load-notebook store tokens)
                   nb-no-seg-id
                   (:meta)))))))

(deftest repo-storage-with-meta
  (let [tokens (creds)
        store (create-storage {:type :repo
                               :user "pink-junkjard"
                               :repo "unittest-notebooks"
                               :filename "unittest-meta2.cljg"})
        meta {:test 456 :id 99}
        nb empty-notebook
        nb (assoc nb :meta meta)]
    (is (= meta
           (do (save-notebook store tokens nb)
               (-> (load-notebook store tokens)
                   nb-no-seg-id
                   (:meta)
                    ;(dissoc :id)
                   ))))))

