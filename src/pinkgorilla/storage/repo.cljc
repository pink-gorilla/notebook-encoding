(ns pinkgorilla.storage.repo
  (:require

   #?(:clj [clojure.tools.logging :refer (info)]
      :cljs [taoensso.timbre :refer-macros (info)])

   #?(:clj [pinkgorilla.storage.github :refer [save-repo load-repo]])

   [clojure.string]
   [pinkgorilla.storage.storage :refer [Storage query-params-to-storage Save Load]]))


(defrecord StorageRepo [user repo filename])

(defmethod query-params-to-storage :repo [_ params]
  (StorageRepo.
   (:user params)
   (:repo params)
   (or (:path params) (:filename params))))


(extend-type StorageRepo
  Storage

  (storagetype [self] :repo)

  (external-url [self]
    (info "repo-storage.external-url")
    nil)

  (gorilla-path [self]
    (info "repo-storage.gorilla-path")
    (str "/edit?source=repo&filename=" (:filename self) "&user=" (:user self) "&repo=" (:repo self))))



#?(:clj

   (extend-type StorageRepo
     Save
     (storage-save [self notebook tokens]
       (let [token (:github-token tokens)]
          (if (or (nil? token) (clojure.string/blank? token) )
            (throw (Exception. (str "NOT Saving Notebook without token: " (:filename self))))
            (if (nil? notebook)
              (throw (Exception. (str "NOT Saving EMPTY Notebook to file: " (:filename self))))
              (do
                (info "Saving Notebook to repo: " (:repo self) " size: " (count notebook))
                (save-repo (:user self) (:repo self) (:filename self) notebook token))))))
     Load
     (storage-load [self tokens]
       (let [token (:github-token tokens)]
         (info "Loading Notebook from repo: " (:repo self) "user: " (:user self) " filename: " (:filename self))
         (if (or (nil? token) (clojure.string/blank? token) )
           (load-repo (:user self) (:repo self) (:filename self))
           (load-repo (:user self) (:repo self) (:filename self) token))))))




