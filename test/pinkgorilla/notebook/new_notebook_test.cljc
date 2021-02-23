(ns pinkgorilla.notebook.new-notebook-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   #?(:clj [taoensso.timbre :refer [info error]]
      :cljs [taoensso.timbre :refer-macros [info error]])
   [pinkgorilla.document.default-config] ; side-effects
   [pinkgorilla.notebook.template  :refer [new-notebook snippets->notebook]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.persistence  :refer [load-notebook save-notebook]]
   [pinkgorilla.encoding.persistence-helper :as helper]))

(deftest encode-new-notebook
  (let [nb (new-notebook)
        f "/tmp/notebook-new.cljg"
        _ (helper/save-notebook f nb)
        nb-reloaded (helper/load-notebook f)]
    (is (= nb nb-reloaded))))

#?(:clj

   (deftest reload-new-notebook
     (let [notebook (new-notebook)
           file-name "/tmp/notebook-new2.cljg"
           storage (create-storage {:type :file
                                    :filename file-name})
        ;_ (info "storage: " storage)
           creds {}
           _ (save-notebook storage creds notebook)
           notebook-reloaded (load-notebook storage creds)]
       (is (= notebook notebook-reloaded))))
;   
   )

#?(:clj

   (deftest snippets-notebook
     (let [notebook-dry (snippets->notebook ["a" "b" "c"])
           notebook-dry-md (snippets->notebook ["a" "b" "c"] "Hello!")]
       (is (= (count (:segments notebook-dry)) 3))
       (is (= (count (:segments notebook-dry-md)) 4))))
   ;
   )