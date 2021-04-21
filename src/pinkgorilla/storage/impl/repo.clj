(ns pinkgorilla.storage.impl.repo
  (:require
   [clojure.string]
   [taoensso.timbre :refer [info error]]
   [pinkgorilla.storage.impl.github :refer [save-repo load-repo]]
   [pinkgorilla.storage.protocols :refer [Save Load]])
  (:import
   [pinkgorilla.storage.repo StorageRepo]))

(extend-type StorageRepo
  Save
  (storage-save [self notebook tokens]
    (if (nil? tokens)
      (throw (Exception. (str "NOT Saving Notebook without token: " (:filename self))))
      (if (nil? notebook)
        (throw (Exception. (str "NOT Saving EMPTY Notebook to file: " (:filename self))))
        (do
          (info "Saving Notebook to repo: " (:repo self) " size: " (count notebook))
          (save-repo (:user self) (:repo self) (:filename self) notebook tokens)))))
  Load
  (storage-load [self tokens]
    (info "Loading Notebook from repo: " (:repo self) "user: " (:user self) " filename: " (:filename self))
    (if (nil? tokens)
      (load-repo (:user self) (:repo self) (:filename self))
      (load-repo (:user self) (:repo self) (:filename self) tokens))))