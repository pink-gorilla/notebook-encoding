(ns pinkgorilla.doo-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   ; modules that contain tests
   [pinkgorilla.bongo]
   [pinkgorilla.dummy]
   [pinkgorilla.encoding.helper-test]
   [pinkgorilla.encoding.simple]
   #_[pinkgorilla.encoding.disk-reload]
   [pinkgorilla.notebook.hydration]
   ))

(doo-tests
 'pinkgorilla.bongo ; cljs only
 'pinkgorilla.dummy ; cljc
 
 ; encoding
 'pinkgorilla.encoding.helper-test
 'pinkgorilla.encoding.simple
 #_'pinkgorilla.encoding.disk-reload
 
 ;notebook
 'pinkgorilla.notebook.hydration)

