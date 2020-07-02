(ns pinkgorilla.notebook.new-notebook-test
  (:require
   #?(:clj [clojure.test :refer :all]
      :cljs  [cljs.test :refer-macros [async deftest is testing]])
   #?(:clj [taoensso.timbre :refer [info error]]
      :cljs [taoensso.timbre :refer-macros [info error]])
   [pinkgorilla.document.default-config] ; side-effects
   [pinkgorilla.notebook.template  :refer [new-notebook]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.hydration  :refer [load-notebook save-notebook hydrate dehydrate]]
   [pinkgorilla.encoding.persistence-helper :as helper]))

(deftest encode-new-notebook
  (let [nb (new-notebook)
        f "/tmp/notebook-new.cljg"
        _ (helper/save-notebook f nb)
        nb-reloaded (helper/load-notebook f)]
    (is (= nb nb-reloaded))))

(deftest reload-new-notebook
  (let [notebook-dry (new-notebook)
        notebook (hydrate notebook-dry)
        ;_ (info "hydrated notebook: " notebook)
        file-name "/tmp/notebook-new2.cljg"
        storage (create-storage {:type :file :filename file-name})
        ;_ (info "storage: " storage)
        creds {}
        _ (save-notebook storage creds notebook)
        notebook-reloaded (load-notebook storage creds)
        notebook-reloaded-dry (dehydrate notebook-reloaded)]
    (is (= notebook-dry notebook-reloaded-dry))))


