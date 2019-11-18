(ns pinkgorilla.storage.repo
  (:require

   #?(:clj [clojure.tools.logging :refer (info)]
      :cljs [taoensso.timbre :refer-macros (info)])

   #?(:clj [pinkgorilla.storage.github :refer [save-repo load-repo]])

   [pinkgorilla.storage.storage :refer [Storage query-params-to-storage Save Load]]))


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



#?(:clj

   (extend-type StorageRepo
     Save
     (storage-save [self notebook tokens]
       (let [token (:github-token tokens)]
         (if (nil? notebook)
           (do
             (info "NOT Saving EMPTY Notebook to file: " (:filename self))
             {:success false :error-message "Notebook is empty"})
           (do
             (info "Saving Notebook to repo: " (:repo self) " size: " (count notebook))
             (save-repo (:user self) (:repo self) (:filename self) notebook token)))))
     Load
     (storage-load [self tokens]
       (let [token (:github-token tokens)]
         (info "Loading Notebook from repo: " (:repo self))
         (if (nil? token)
           (load-repo (:user self) (:repo self) (:filename self))
           (load-repo (:user self) (:repo self) (:filename self) token))))))




