(ns pinkgorilla.storage.repo
  (:require
   [clojure.string]
   #?(:clj [taoensso.timbre :refer [info error]]
      :cljs [taoensso.timbre :refer-macros [info]])
   [pinkgorilla.storage.filename-encoding :refer [filename->encoding filename->name]]
   #?(:clj [pinkgorilla.storage.github :refer [save-repo load-repo]])
   [pinkgorilla.storage.protocols :refer [FromFilename Storage query-params-to-storage Save Load]]))

(defrecord StorageRepo [user repo filename])

(defmethod query-params-to-storage :repo [_ params]
  (StorageRepo.
   (:user params)
   (:repo params)
   (:filename params)))

(extend-type StorageRepo
  Storage
  (storagetype [self] :repo)
  (external-url [self]
    ;https://github.com/pink-junkjard/tailwind-workstation-screencast/blob/master/src/workation/core.cljs
    (str "https://github.com/"
         (:user self) "/"
         (:repo self) "/blob/master/"
         (:filename self)))

  (gorilla-path [self]
    (str "?source=repo"
         "&filename=" (:filename self)
         "&user=" (:user self)
         "&repo=" (:repo self))))

#?(:clj

   (extend-type StorageRepo
     Save
     (storage-save [self notebook tokens]
       (let [token (:github-token tokens)]
         (if (or (nil? token) (clojure.string/blank? token))
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
         (if (or (nil? token) (clojure.string/blank? token))
           (load-repo (:user self) (:repo self) (:filename self))
           (load-repo (:user self) (:repo self) (:filename self) token))))))

(extend-type StorageRepo
  FromFilename
  (determine-encoding [this]
    (filename->encoding this :filename))
  (determine-name [this]
    (filename->name this :filename)))


