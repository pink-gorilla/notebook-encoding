(ns pinkgorilla.import.import-test
  (:require
   [clojure.test :refer :all]
   ; dependencies needed to be in cljs bundle:  
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.storage.file]
   ; pinkgorilla
   [pinkgorilla.import.clj-import :refer [read-forms clj->notebook clj->convert file->topforms-with-metadata clj->notebook-marginalia]]
   [pinkgorilla.notebook.core :refer [notebook-load]]
   [pinkgorilla.import.convert-main :refer [to-gorilla]]))

(def example-code
  ";; # Test
   (def a 2) (def b 3) (defn add-two [x y] (+ x y)) (println (add-two a b))")

(def example-forms
  '((def a 2)
    (def b 3)
    (defn add-two [x y] (+ x y))
    (println (add-two a b))))

(deftest forms-import-test
  (let [file-name  "/tmp/import-test.clj"
        _ (spit file-name example-code)
        example-forms-str (map str example-forms)
        forms (read-forms file-name)]
    (is (= example-forms-str forms))))

(deftest import-clj-notebook
  (let [file-name  "/tmp/import-test.clj"
        _ (spit file-name example-code)
        notebook (clj->notebook file-name)]
    (is (= (count (:segments notebook)) 5))))

(deftest import-clj-reload
  (let [file-name  "/tmp/import-test.clj"
        _ (spit file-name example-code)
        _ (clj->convert file-name)
        storage (storage/create-storage {:type :file
                                         :filename "/tmp/import-test.cljg"})
        tokens {}
        notebook (notebook-load storage tokens)]
    (is (= (count (:segments notebook)) 5))))

(comment
  (read-forms "/home/andreas/Documents/gorilla/python-gorilla/resources/notebooks/test.clj")
  (read-forms "/home/andreas/Documents/gorilla/python-gorilla/resources/notebooks/techml-datatable-dplyr.clj")

  (map meta
       (file->topforms-with-metadata "/home/andreas/Documents/gorilla/python-gorilla/resources/notebooks/techml-datatable-dplyr.clj"))

  (map meta
       (file->topforms-with-metadata "/home/andreas/Documents/gorilla/python-gorilla/resources/notebooks/test.clj"))

  (clj->notebook  "/tmp/import-test.clj")

  (clj->convert             "/home/andreas/Documents/gorilla/clojisr-gorilla/resources/notebooks/datatable_dplyr.clj")
  (clj->notebook-marginalia "/home/andreas/Documents/gorilla/clojisr-gorilla/resources/notebooks/datatable_dplyr.clj")
  (to-gorilla "test/notebooks/basic-concepts.ipynb")

 ; 
  )

