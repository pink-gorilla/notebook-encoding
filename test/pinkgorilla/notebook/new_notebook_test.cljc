(ns pinkgorilla.notebook.new-notebook-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   #?(:clj [taoensso.timbre :refer [info error]]
      :cljs [taoensso.timbre :refer-macros [info error]])
   [pinkgorilla.encoding.default-config] ; side-effects
   [notebook.template  :refer [make-notebook snippets->notebook]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.persistence  :refer [load-notebook save-notebook]]
   [pinkgorilla.encoding.persistence-helper :as helper]))

(defn nb-no-seg-id [nb]
  (let [segs (:segments nb)
        segs-no-id (into []
                         (map #(dissoc % :id :state) segs))]
    (assoc nb :segments segs-no-id)))

(deftest encode-new-notebook
  (let [nb (make-notebook)
        nb-no-id (nb-no-seg-id nb)
        f "/tmp/notebook-new.cljg"
        _ (helper/save-notebook f nb)
        nb-reloaded (helper/load-notebook f)
        nb-reloaded (nb-no-seg-id nb-reloaded)]
    (is (= nb-no-id nb-reloaded))))

#?(:clj

   (deftest reload-new-notebook
     (let [notebook (make-notebook)
           nb-no-id (nb-no-seg-id notebook)
           file-name "/tmp/notebook-new2.cljg"
           storage (create-storage {:type :file
                                    :filename file-name})
        ;_ (info "storage: " storage)
           creds {}
           _ (save-notebook storage creds notebook)
           notebook-reloaded (load-notebook storage creds)
           notebook-reloaded-no-id (nb-no-seg-id notebook-reloaded)]
       (is (= nb-no-id notebook-reloaded-no-id))))
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


