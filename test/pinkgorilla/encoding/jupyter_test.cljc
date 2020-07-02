(ns pinkgorilla.encoding.jupyter-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.storage.filename-encoding :refer [split-filename]]
   [pinkgorilla.encoding.protocols :refer [decode]]
   [pinkgorilla.encoding.persistence-helper :refer [load-notebook save-notebook]]))

(defn filename->encoding [filename]
  (:encoding (split-filename filename)))

(deftest format-detection
  (is (= :gorilla (filename->encoding "demo.cljg")))
  (is (= :jupyter (filename->encoding "../../quant/trateg/notebooks/basic-concepts.ipynb")))
  (is (= :gorilla (filename->encoding "../../quant/trateg/notebooks/basic-concepts.cljg"))))

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