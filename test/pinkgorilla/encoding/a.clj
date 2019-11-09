(ns pinkgorilla.encoding.a
  (:require
   ;[cljs.test :refer-macros [async deftest is testing]]
   [clojure.string :as str]
   [pinkgorilla.encoding.decode :refer [decode]]
   [pinkgorilla.encoding.encode :refer [encode-notebook]]))


(defn load-notebook [f]
  (let [s (slurp f)]
    (decode s)))

(defn save-notebook [f notebook]
  (let [s (encode-notebook notebook)]
    (spit f s)))

  ; (def nb (slurp "resources/notebook1.cljw"))
;(def nb (slurp "resources/demo.cljw"))
(def f "resources/reagent-manipulate.clj")

(let [notebook (load-notebook f)
      f-out "/tmp/notebook-unittest.cljw"
      _ (save-notebook f-out notebook)
      ]
  true)


