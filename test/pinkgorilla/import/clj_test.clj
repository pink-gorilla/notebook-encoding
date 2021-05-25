(ns pinkgorilla.import.clj-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.encoding.default-config] ; side effects
   [pinkgorilla.encoding.protocols :refer [decode]]
   [pinkgorilla.import.convert-main :refer [to-gorilla]]
   [pinkgorilla.encoding.clj :refer [read-forms str->topforms-with-metadata]]))

(def example-code
  ";; # Test
   (def a 2) (def b 3) (defn add-two [x y] (+ x y)) (println (add-two a b))")

(def example-forms
  '((def a 2)
    (def b 3)
    (defn add-two [x y] (+ x y))
    (println (add-two a b))))

#_(deftest forms-import-test
    (let [file-name  "/tmp/import-test.clj"
          _ (spit file-name example-code)
          example-forms-str (map str example-forms)
          forms (read-forms file-name)]
      (is (= example-forms-str forms))))

(deftest decode-clj
  (let [notebook (decode :clj example-code)]
    (is (= (count (:segments notebook)) 4)))) ; only code segments


(comment
  (read-forms "/home/andreas/Documents/gorilla/python-gorilla/resources/notebooks/test.clj")
  (read-forms "/home/andreas/Documents/gorilla/python-gorilla/resources/notebooks/techml-datatable-dplyr.clj")

  (map meta
       (str->topforms-with-metadata "/home/andreas/Documents/gorilla/python-gorilla/resources/notebooks/techml-datatable-dplyr.clj"))

  (map meta
       (str->topforms-with-metadata "/home/andreas/Documents/gorilla/python-gorilla/resources/notebooks/test.clj"))

  (decode :clj  "/tmp/import-test.clj")

  ;(clj->convert             "/home/andreas/Documents/gorilla/clojisr-gorilla/resources/notebooks/datatable_dplyr.clj")

 ; 
  )

