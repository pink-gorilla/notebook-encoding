(ns pinkgorilla.storage.repo
  (:require
   [clojure.string]
   [pinkgorilla.storage.filename-encoding :refer [filename->encoding filename->name]]
   [pinkgorilla.storage.protocols :refer [FromFilename Storage create-storage]]))

(defrecord StorageRepo [user repo filename])

(defmethod create-storage :repo [params]
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
         (:filename self))))

(extend-type StorageRepo
  FromFilename
  (determine-encoding [this]
    (filename->encoding this :filename))
  (determine-name [this]
    (filename->name this :filename)))


