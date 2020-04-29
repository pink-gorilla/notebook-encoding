(ns pinkgorilla.import.convert-main
  (:require
   [pinkgorilla.import.clj-import :refer [clj->convert]])
  (:gen-class))

(defn -main [file-name]
  (println "converting clj file: " file-name)
  (clj->convert file-name))
