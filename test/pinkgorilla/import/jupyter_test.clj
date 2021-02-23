
(ns pinkgorilla.import.jupyter-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.persistence :refer [load-notebook]]
   [pinkgorilla.import.convert-main :refer [to-gorilla]]))

(deftest import-jupyter-reload
  (let [file-name-original  "test/notebooks/basic-concepts.ipynb"
        file-name "/tmp/basic.ipynb"
        _ (spit file-name (slurp file-name-original))
        _ (to-gorilla file-name) ; this creates cljg file
        storage (create-storage {:type :file
                                 :filename "/tmp/basic.cljg"})
        tokens {}
        notebook (load-notebook storage tokens)]
    (is (= (count (:segments notebook)) 86))))


