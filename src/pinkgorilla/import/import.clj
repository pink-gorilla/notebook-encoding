(ns pinkgorilla.import.import
  (:require
   [clojure.string :as str]
   [pinkgorilla.encoding.protocols :refer [decode encode]]
   #_[pinkgorilla.storage.file]))

(defn save-notebook [f notebook]
  (let [s (encode :gorilla notebook)]
    (spit f s)))

(defn jupyter-convert [file-name]
  (let [notebook (decode :jupyter (slurp file-name))
        filename-notebook (clojure.string/replace file-name ".cljg")]
    (save-notebook filename-notebook notebook)))

(defn to-notebook [format re-extension file-name]
  (let [str (slurp file-name)
        notebook (decode format str)
        filename-notebook (str/replace file-name re-extension ".cljg")]
    (save-notebook filename-notebook notebook)))