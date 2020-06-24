(ns pinkgorilla.storage.filename-encoding
  (:require
   #?(:clj [clojure.tools.logging :refer [info]]
      :cljs [taoensso.timbre :refer-macros [info]])
   [clojure.string :as str]
   [pinkgorilla.encoding.protocols :refer [decode]]
   [pinkgorilla.storage.protocols :refer [determine-encoding]]
 ;  #?(:clj [pinkgorilla.storage.file :refer [StorageFile]])
 ;  [pinkgorilla.storage.gist :refer [StorageGist]]
   ;[pinkgorilla.storage.repo :refer [StorageRepo]]
   ))

(defn extension->encoding [extension]
  (case (str/lower-case extension)
    "cljg" :gorilla
    "ipynb" :jupyter
    "clj" :clojure
    :unknown))

(defn split-filename
  "extracts the only the name of the file, without extension and path"
  [file-name]
  (when-not (str/blank? file-name)
    (when-let [; the regex returns [full-hit name-only]
               match (re-find #"(?i)(.+?)([\w-]*).(cljg|ipynb)*$" file-name)]
      (let [[full path name ext] match]
        {:full full
         :path path
         :name name
         :ext ext
         :encoding (extension->encoding ext)}))))

(defn filename->encoding [this k]
  (info "this: " this)
  (:encoding (split-filename (k this))))

(defn filename->name [this k]
  (:name (split-filename (k this))))

(defn decode-storage-using-filename [storage content]
  (info "Decoding: " (count content) " bytes")
  (when (and storage content)
    (when-let [encoding-type (determine-encoding storage)]
      (info "determined encoding: " encoding-type)
      (when-let [notebook (decode encoding-type content)]
        (info "successfully parsed notebook! ") ;  notebook)
        (info notebook)
        notebook))))

(comment

  (split-filename "/home/andreas/demo1.cljg")
  (split-filename "/home/andreas/demo1.Cljg"))

