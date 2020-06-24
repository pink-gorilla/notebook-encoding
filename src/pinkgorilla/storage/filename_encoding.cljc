(ns pinkgorilla.storage.filename-encoding
  (:require
   #?(:clj [clojure.tools.logging :refer [trace debug info]]
      :cljs [taoensso.timbre :refer-macros [trace debug info]])
   [clojure.string :as str]
   [pinkgorilla.encoding.protocols :refer [decode]]
   [pinkgorilla.storage.protocols :refer [determine-encoding]]))

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
  (debug "Decoding: " (count content) " bytes")
  (when (and storage content)
    (when-let [encoding-type (determine-encoding storage)]
      (debug "determined encoding: " encoding-type)
      (when-let [notebook (decode encoding-type content)]
        (debug "successfully parsed notebook! ") ;  notebook)
        (trace notebook)
        notebook))))

(comment

  (split-filename "/home/andreas/demo1.cljg")
  (split-filename "/home/andreas/demo1.Cljg"))

