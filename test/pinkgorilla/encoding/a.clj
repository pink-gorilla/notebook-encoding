(ns pinkgorilla.encoding.a
  (:require
   ;[cljs.test :refer-macros [async deftest is testing]]
   [clojure.string :as str]
   [instaparse.core :as insta]
   [pinkgorilla.encoding.decode :refer [decode]]))

; (def nb (slurp "resources/notebook1.cljw"))
;(def nb (slurp "resources/demo.cljw"))
(def nb-str (slurp "resources/reagent-manipulate.clj"))


(decode nb-str)


