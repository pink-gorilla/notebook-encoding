(ns pinkgorilla.encoding.doo-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   ; modules that contain tests
   [pinkgorilla.encoding.bongo]
   [pinkgorilla.encoding.dummy]
   [pinkgorilla.encoding.helper-test]
   [pinkgorilla.encoding.simple]
   #_[pinkgorilla.encoding.disk-reload]
   ))

(doo-tests
 'pinkgorilla.encoding.bongo
 'pinkgorilla.encoding.dummy
 'pinkgorilla.encoding.helper-test
 'pinkgorilla.encoding.simple
 #_'pinkgorilla.encoding.disk-reload)

