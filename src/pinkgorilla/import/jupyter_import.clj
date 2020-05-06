(ns pinkgorilla.import.jupyter-import
  (:require
   [clojure.string]
   [pinkgorilla.storage.storage :refer [create-storage] :as storage]
   [pinkgorilla.storage.file]
   [pinkgorilla.encoding.decode :refer [decode]]
   [pinkgorilla.encoding.jupyter] ; bring to scope
   [pinkgorilla.encoding.encode :refer [encode-notebook]]
   ; pinkgorilla
   ;[pinkgorilla.encoding.persistence :refer [save-notebook]]
   ))

(defn save-notebook [f notebook]
  (let [s (encode-notebook notebook)]
    (spit f s)))

(defn jupyter->convert [file-name]
  (let [notebook (decode :jupyter (slurp file-name))
        filename-notebook (clojure.string/replace file-name #".ipynb" ".cljg")]
    (save-notebook filename-notebook notebook)))

