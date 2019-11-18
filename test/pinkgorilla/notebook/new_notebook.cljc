(ns pinkgorilla.notebook.new-notebook
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.encoding.persistence :refer [load-notebook save-notebook]]
   [pinkgorilla.notebook.core  :refer [dehydrate-notebook]]
   [pinkgorilla.notebook.newnb  :refer [create-new-worksheet]]
   )
)


(deftest encode-new-notebook
  (let [nb-hydrated (create-new-worksheet)
        nb (dehydrate-notebook nb-hydrated)
        f "/tmp/notebook-new.cljg"
        _ (save-notebook f nb)
        nb-reloaded (load-notebook f)
        ;_ (println "reloaded notebook: " nb-reloaded)
        ]
    (is (= nb nb-reloaded))))


(comment
  (create-new-worksheet)
  )

