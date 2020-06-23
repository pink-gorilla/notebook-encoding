(ns pinkgorilla.import.marginalia
  (:require
   [clojure.string :as str]
   [pinkgorilla.encoding.protocols :refer [decode encode]]
   [pinkgorilla.storage.file]))

(defn save-notebook [f notebook]
  (let [s (encode :gorilla notebook)]
    (spit f s)))

(defn marginalia-convert [file-name]
  (let [str (slurp file-name)
        notebook (decode :marginalia str)
        filename-notebook (clojure.string/replace file-name #".clj" ".cljg")]
    (save-notebook filename-notebook notebook)))
