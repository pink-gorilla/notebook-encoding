(ns pinkgorilla.storage.protocols
  (:require
   #?(:clj [taoensso.timbre :refer [info error]]
      :cljs [taoensso.timbre :refer-macros [info error]])))

#?(:clj (defmulti create-storage (fn [m] (:type m)))
   :cljs (defmulti create-storage (fn [m] (:type m))))

(defmethod create-storage :default [m]
  (let [t (or (:type m) :type-unspecified)]
    (error "ERROR: unknown storage type: " t " with params: " m)
    nil))

(defn storage->map [s]
  (into {} s))

(defprotocol Storage
  (storagetype [self]) ; :file :repo :gist :bitbucket
  (external-url [self]) ; to view raw persisted data in browser.
  )

(defprotocol Save
  (storage-save [self notebook tokens]))

(defprotocol Load
  (storage-load [self tokens]))

(defprotocol FromFilename
  (determine-encoding [self])
  (determine-name [self]))
