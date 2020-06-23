(ns pinkgorilla.storage.bitbucket
  (:require
   #?(:clj [clojure.tools.logging :refer [info]]
      :cljs [taoensso.timbre :refer-macros [info]])
   [pinkgorilla.storage.storage :refer [Storage query-params-to-storage]]))

(defrecord StorageBitbucket [user repo revision path])

(defmethod query-params-to-storage :bitbucket [_ params]
  (StorageBitbucket.
   (:user params)
   (:repo params)
   (or (:revision params) "HEAD")
   (:path params)))

(extend-type StorageBitbucket
  Storage

  (storagetype [self] :bitbucket)

  (storageformat [self] :gorilla)

  (external-url [self]
    (info "local-storage.external-url")
    nil)

  (gorilla-path
    [self]
    (info "bitbucket.gorilla-path")
    ;; TODO This appears to be broken!
    (str "/edit?worksheet-filename=" (:user self))))

