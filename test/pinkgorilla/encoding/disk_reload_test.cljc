(ns pinkgorilla.encoding.disk-reload-test
  "test encoding by loading a notebook from disk, then saving it to
   a temporary file and then loading the temporary file. 

   currently only working for clj
   TODO: cljs inject test notebooks via macros to the bundle."
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.encoding.default-config] ; side effects
   [pinkgorilla.encoding.persistence-helper :refer [load-notebook save-notebook]]))


  ; (def nb (slurp "test/notebooks/notebook1.cljg"))
;(def nb (slurp "test/notebooks/demo.cljg"))


(defn nb-no-seg-id [nb]
  (let [segs (:segments nb)
        segs-no-id (into []
                         (map #(dissoc % :id) segs))]
    (assoc nb :segments segs-no-id)))

(def f "test/notebooks/reagent-manipulate.cljg")

(deftest reload-existing-notebook
  (let [notebook (load-notebook f)
        f-out "/tmp/notebook-unittest.cljg"
        _ (save-notebook f-out notebook)
        notebook-reloaded (load-notebook f-out)
        notebook-reloaded (nb-no-seg-id  notebook-reloaded)
        notebook (nb-no-seg-id notebook)]
    (is (= notebook notebook-reloaded))))

(def f2 "test/notebooks/diff.cljg")
#_(deftest reload-existing-notebook
    (let [notebook (load-notebook f2)
          f-out "/tmp/notebook-unittest2.cljg"
          _ (save-notebook f-out notebook)
          notebook-reloaded (load-notebook f-out)]
      (is (= notebook notebook-reloaded))))

(comment
  (let [notebook (load-notebook f)]
    (count notebook)
;    (nth (:segments notebook) 0)
    ))
