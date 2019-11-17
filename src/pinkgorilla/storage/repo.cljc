(ns pinkgorilla.storage.repo
  (:require

   #?(:clj [clojure.tools.logging :refer (info)]
      :cljs [taoensso.timbre :refer-macros (info)])

   [pinkgorilla.storage.storage :refer [Storage query-params-to-storage]]))


(defrecord StorageRepo [user repo path])

(defmethod query-params-to-storage :repo [_ params]
  (StorageRepo.
   (:user params)
   (:repo params)
   (:path params)))


(extend-type StorageRepo
  Storage

  (storagetype [self] :repo)

  (external-url [self]
    (info "repo-storage.external-url")
    nil)

  (gorilla-path [self]
    (info "repo-storage.gorilla-path")
    (str "/edit?worksheet-filename=" (:id self))))


