(ns pinkgorilla.storage.unsaved
  (:require
   #?(:clj [taoensso.timbre :refer [info]]
      :cljs [taoensso.timbre :refer-macros [info]])
   [clojure.string]
   [pinkgorilla.storage.protocols :refer [Storage query-params-to-storage FromFilename]]))

(defrecord StorageUnsaved [id])

(defmethod query-params-to-storage :unsaved [_ params]
  (StorageUnsaved. (:id params)))

(extend-type StorageUnsaved
  Storage
  (storagetype [self] :unsaved)
  (external-url [self]
    nil)
  (gorilla-path [self]
    (str "?source=unsaved"
         "&id=" (:id self))))

(extend-type StorageUnsaved
  FromFilename
  (determine-encoding [this]
    :gorilla)
  (determine-name [this]
    (:id this)))


