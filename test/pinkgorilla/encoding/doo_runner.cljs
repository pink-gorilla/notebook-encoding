(ns pinkgorilla.encoding.doo-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [pinkgorilla.encoding.bongo]
  ; [pinkgorilla.encoding.dummy]
  ; [pinkgorilla.encoding.helper-test]
  ; [pinkgorilla.encoding.simple]
  ; [pinkgorilla.encoding.disk-reload]
   ))


(println "doo cljs tests..")

(doo-tests 'pinkgorilla.encoding.bongo
           ;'[pinkgorilla.encoding.dummy]
           ;'[pinkgorilla.encoding.helper-test]
           ;'[pinkgorilla.encoding.simple]
           ;'[pinkgorilla.encoding.disk-reload]
 )

