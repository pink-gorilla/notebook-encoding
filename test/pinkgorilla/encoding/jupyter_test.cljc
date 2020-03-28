(ns pinkgorilla.encoding.jupyter-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.storage.file :refer [filename-format]]
   [pinkgorilla.encoding.jupyter] ; bring multimethods to bundle
   [pinkgorilla.encoding.decode :refer [decode]]
   [pinkgorilla.encoding.persistence :refer [load-notebook save-notebook]]))

(deftest format-detection
  (is (= :gorilla (filename-format "demo.cljg")))
  (is (= :jupyter (filename-format "../../quant/trateg/notebooks/basic-concepts.ipynb")))
  (is (= :gorilla (filename-format "../../quant/trateg/notebooks/basic-concepts.cljg"))))

#_(deftest encode-simple
    (let [f "/tmp/notebook-simple.cljg"
          _ (save-notebook f notebook-simple)]
      (is (= notebook-simple (load-notebook f)))))

#?(:clj
   (deftest jupyter-decoding
     (is (= "J" (:version
                 (decode
                  :jupyter
                  (slurp "test/notebooks/basic-concepts.ipynb")))))))