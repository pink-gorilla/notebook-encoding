(ns pinkgorilla.import.marginalia-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.encoding.protocols :refer [decode]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.persistence :refer [load-notebook]]
   [pinkgorilla.import.convert-main :refer [to-gorilla]]
   [pinkgorilla.encoding.marginalia] ; side-effects
   ))

(def example-code
  ";; k  k  hkj kj \r\n
   (def a 2) (def b 3) (defn add-two [x y] (+ x y)) (println (add-two a b))")

(def example-forms
  '((def a 2)
    (def b 3)
    (defn add-two [x y] (+ x y))
    (println (add-two a b))))

(defn code-segment-count [notebook]
  (->> (:segments notebook)
       (filter (fn [s]
                 ;(println s)
                 (= :code (:type s))))
       count))

(deftest decode-marginalia
  (let [notebook (decode :marginalia example-code)]
    (is (= (code-segment-count notebook) 4))))

(deftest import-marginalia-reload
  (let [file-name  "/tmp/import-marginalia.clj"
        _ (spit file-name example-code)
        _ (to-gorilla file-name) ; this creates cljg file
        storage (create-storage {:type :file
                                 :filename "/tmp/import-marginalia.cljg"})
        tokens {}
        notebook (load-notebook storage tokens)]
    (is (= (code-segment-count notebook) 4))))

(comment
  ;(marginalia-convert "/home/andreas/Documents/gorilla/clojisr-gorilla/resources/notebooks/datatable_dplyr.clj")

 ; 
  )

