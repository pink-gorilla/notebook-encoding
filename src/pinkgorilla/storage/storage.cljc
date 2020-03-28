(ns pinkgorilla.storage.storage)

#?(:clj (defmulti query-params-to-storage (fn [t p] t))
   :cljs (defmulti query-params-to-storage identity))

; class not found exception - but this should work ??? 
; #?(:clj [clojure.tools.logging :refer (info)]
;   :cljs [taoensso.timbre :refer-macros (info)])

(defmethod query-params-to-storage :default [t params]
  (println "ERROR: unknown storage type: " t " with params: " params)
  nil)

(defn create-storage [params]
  (let [stype (:type params)]
    (if (nil? stype)
      (do (println "cannot create storage from nil params")
          nil)
      (query-params-to-storage stype params))))

(defprotocol Storage

  (storagetype [self]) ; :file :repo :gist :bitbucket

  (storageformat [self]) ; gorilla :jupyter

  (external-url [self]) ; to view raw persisted data in browser.

  (gorilla-path [self]) ; to open a notebook from the sidebar
  )

(defprotocol Save
  (storage-save [self notebook tokens]))

(defprotocol Load
  (storage-load [self tokens]))