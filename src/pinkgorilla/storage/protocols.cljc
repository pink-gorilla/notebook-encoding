(ns pinkgorilla.storage.protocols
  (:require
   #?(:clj [taoensso.timbre :refer [info error]]
      :cljs [taoensso.timbre :refer-macros [info error]])))

#?(:clj (defmulti query-params-to-storage (fn [t p] t))
   :cljs (defmulti query-params-to-storage identity))

(defmethod query-params-to-storage :default [t params]
  (error "ERROR: unknown storage type: " t " with params: " params)
  nil)

(defn create-storage [params]
  (let [stype (:type params)]
    (if (nil? stype)
      (do (error "cannot create storage from nil params")
          nil)
      (query-params-to-storage stype params))))

(defprotocol Storage
  (storagetype [self]) ; :file :repo :gist :bitbucket
  (external-url [self]) ; to view raw persisted data in browser.
  (gorilla-path [self])) ; to open a notebook from the sidebar


(defprotocol Save
  (storage-save [self notebook tokens]))

(defprotocol Load
  (storage-load [self tokens]))

(defprotocol FromFilename
  (determine-encoding [self])
  (determine-name [self]))
