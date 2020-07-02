(ns pinkgorilla.import.marginalia-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.encoding.protocols :refer [decode]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.hydration :refer [load-notebook]]
   [pinkgorilla.import.marginalia :refer [marginalia-convert]]
   [pinkgorilla.import.convert-main :refer [to-gorilla]]))

(def example-code
  ";; k  k  hkj kj \r\n
   (def a 2) (def b 3) (defn add-two [x y] (+ x y)) (println (add-two a b))")

(def example-forms
  '((def a 2)
    (def b 3)
    (defn add-two [x y] (+ x y))
    (println (add-two a b))))

(deftest decode-marginalia
  (let [notebook (decode :marginalia example-code)]
    (is (= (count (:segments notebook)) 5))))

(deftest import-marginalia-reload
  (let [file-name  "/tmp/import-test.clj"
        _ (spit file-name example-code)
        _ (marginalia-convert file-name) ; this creates cljg file
        storage (create-storage {:type :file
                                 :filename "/tmp/import-test.cljg"})
        tokens {}
        notebook (load-notebook storage tokens)]
    (is (= (count (:segments notebook)) 5))))

(comment
  (marginalia-convert "/home/andreas/Documents/gorilla/clojisr-gorilla/resources/notebooks/datatable_dplyr.clj")

 ; 
  )

