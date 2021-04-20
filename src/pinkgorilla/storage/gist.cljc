(ns pinkgorilla.storage.gist
  (:require
   [clojure.string]
   [pinkgorilla.storage.filename-encoding :refer [filename->encoding filename->name]]
   [pinkgorilla.storage.protocols :refer [FromFilename Storage create-storage Save Load]]))

(defrecord StorageGist [id filename user is-public description])

(defn hack-null
  "hack: on server query params in post-body arrive as null as string "
  [id]
  (if (nil? id)
    nil
    (if (= "null" id)
      nil
      id)))

(defmethod create-storage :gist [params]
  (StorageGist.
   (or (hack-null (:id params)) nil)
   (:filename params)
   (or (:user params) "")
   (or (:is-public params) true)
   (or (:description params) "")))

(extend-type StorageGist
  Storage
  (storagetype [self] :gist)
  (external-url [self]
    ;https://gist.github.com/awb99/55b101d84d9b3814c46a4f9fbadcf2f8
    (str "https://gist.github.com/" (:user self) "/" (:id self))))

(extend-type StorageGist
  FromFilename
  (determine-encoding [this]
    (filename->encoding this :filename))
  (determine-name [this]
    (filename->name this :filename)))

