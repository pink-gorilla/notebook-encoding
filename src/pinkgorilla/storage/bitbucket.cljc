(ns pinkgorilla.storage.bitbucket
  (:require
   [pinkgorilla.storage.protocols :refer [Storage create-storage]]))

(defrecord StorageBitbucket [user repo revision path])

(defmethod create-storage :bitbucket [params]
  (StorageBitbucket.
   (:user params)
   (:repo params)
   (or (:revision params) "HEAD")
   (:path params)))

(extend-type StorageBitbucket
  Storage
  (storagetype [self] :bitbucket)
  (external-url [self] nil))

