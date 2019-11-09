(ns pinkgorilla.encoding.disk-reload
  "test encoding by loading a notebook from disk, then saving it to
   a temporary file and then loading the temporary file. 

   currently oly working for clj
   TODO: cljs inject test notebooks via macros to the bundle.
  "
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.encoding.persistence :refer [load-notebook save-notebook]]))


  ; (def nb (slurp "resources/notebook1.cljw"))
;(def nb (slurp "resources/demo.cljw"))
(def f "resources/reagent-manipulate.cljw")


(deftest reload-existing-notebook
  (let [notebook (load-notebook f)
        f-out "/tmp/notebook-unittest.cljw"
        _ (save-notebook f-out notebook)
        notebook-reloaded (load-notebook f-out)]
    (is (= notebook notebook-reloaded))))


(comment
  (let [notebook (load-notebook f)]
    (count notebook)
;    (nth (:segments notebook) 0)
    ))
