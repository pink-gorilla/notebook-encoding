(ns pinkgorilla.encoding.doo-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]

[pinkgorilla.encoding.a]
   
   ))


(println "doo cljs tests..")

(doo-tests 'pinkgorilla.encoding.a
           ;'your-project.util-test
           )

