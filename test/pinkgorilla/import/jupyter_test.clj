
(ns pinkgorilla.import.jupyter-test
  (:require
   [clojure.test :refer :all]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.hydration :refer [notebook-load]]
   [pinkgorilla.import.convert-main :refer [to-gorilla]]))

(deftest import-jupyter-reload
  (let [file-name-original  "test/notebooks/basic-concepts.ipynb"
        file-name "/tmp/basic.ipynb"
        _ (spit file-name (slurp file-name-original))
        _ (to-gorilla file-name) ; this creates cljg file
        storage (create-storage {:type :file
                                 :filename "/tmp/basic.cljg"})
        tokens {}
        notebook (notebook-load storage tokens)]
    (is (= (count (:segments notebook)) 86))))


