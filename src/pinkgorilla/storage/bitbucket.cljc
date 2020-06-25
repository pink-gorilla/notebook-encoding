(ns pinkgorilla.storage.bitbucket
  (:require
   #?(:clj [clojure.tools.logging :refer [info]]
      :cljs [taoensso.timbre :refer-macros [info]])
   [pinkgorilla.storage.protocols :refer [Storage query-params-to-storage]]))

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
    nil)

  (gorilla-path
    [self]
    (str "?source=bitbucket"
         "&user=" (:user self)
         "&repo=" (:repo self)
         "&path=" (:path self)
         "&revision=" (:revision self))))

