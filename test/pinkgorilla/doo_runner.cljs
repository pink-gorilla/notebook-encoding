(ns pinkgorilla.doo-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   ; modules that contain tests
   [pinkgorilla.bongo]
   [pinkgorilla.dummy]
   ;encoding
   [pinkgorilla.encoding.helper-test]
   [pinkgorilla.encoding.decoding-bad-format]
   [pinkgorilla.encoding.simple]
   [pinkgorilla.encoding.disk-reload]
   ;notebook
   [pinkgorilla.notebook.hydration]
   [pinkgorilla.notebook.new-notebook]
   ))

(doo-tests
 'pinkgorilla.bongo ; cljs only
 'pinkgorilla.dummy ; cljc
 
 ; encoding
 'pinkgorilla.encoding.helper-test
 'pinkgorilla.encoding.decoding-bad-format
 'pinkgorilla.encoding.simple
 'pinkgorilla.encoding.disk-reload
 
 ;notebook
 'pinkgorilla.notebook.hydration
 'pinkgorilla.notebook.new-notebook)

