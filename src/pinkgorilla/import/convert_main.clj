(ns pinkgorilla.import.convert-main
  (:require
   [pinkgorilla.import.clj-import :refer [clj->convert]])
  (:gen-class))

(defn -main [& args]
  (println "nbconvert args: " args)
  (let [file-name (last args)]
    (println "converting clj file: " file-name)
    (clj->convert file-name)))
