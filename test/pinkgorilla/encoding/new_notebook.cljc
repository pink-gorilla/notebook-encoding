(ns pinkgorilla.encoding.new-notebook
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   [pinkgorilla.encoding.persistence :refer [load-notebook save-notebook]]
   [pinkgorilla.notebook.new  :refer [create-new-worksheet]]
   )
)


(deftest encode-new-notebook
  (let [nb (create-new-worksheet)
        f "/tmp/notebook-new.cljw"
        _ (save-notebook f nb)]
    (is (= nb (load-notebook f)))))


(comment
  (create-new-worksheet)
  )

