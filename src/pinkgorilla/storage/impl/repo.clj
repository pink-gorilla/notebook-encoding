(ns pinkgorilla.storage.impl.repo
  (:require
   [clojure.string]
   [taoensso.timbre :refer [trace debug info error]]
   [tentacles.repos]
   [pinkgorilla.storage.protocols :refer [Save Load]])
  (:import
   [pinkgorilla.storage.repo StorageRepo]))

(defn load-repo-raw [user repo path & [tokens]]
  (if (nil? tokens)
    (tentacles.repos/contents user repo path {:str? true})
    (tentacles.repos/contents user repo path (merge {:str? true} tokens))))

(defn load-repo [user repo path & [tokens]]
  (:content
   (if (nil? tokens)
     (tentacles.repos/contents user repo path {:str? true})
     (tentacles.repos/contents user repo path (merge {:str? true}  tokens)))))


;; body :message


#_{:content {:path "bongo.txt"
             :name "bongo.txt"
             :type "file"
             :size 8
             :sha "0839b2e9412b314cb8bb9a20f587aa13752ae310"}
   :commit {:tree {:sha "328d0c1bf3bc3c49f53a9c13270db1d6d6590b4d"
                   :url "https://api.github.com/repos/pink-junkjard/unittest-notebooks/git/trees/328d0c1bf3bc3c49f53a9c13270db1d6d6590b4d"}
            :committer {:name "awb99", :email "10854682+awb99@users.noreply.github.com"
                        :date "2021-04-22T01:20:56Z"}
            :node_id "MDY6Q29tbWl0MjE2MzM3MjExOmE2M2JlYjQ4Y2EwMWM5NzIzN2FlMzFiZWE3NDU5YmIxNDRiMTc0MTk="
            :author {:name "awb99", :email "10854682+awb99@users.noreply.github.com", :date "2021-04-22T01:20:56Z"}
            :parents [{:sha "5ab0e21776682694b005716917c65876f43bfd01"}]
            :sha "a63beb48ca01c97237ae31bea7459bb144b17419"
            :message "pinkgorilla notebook save"}}

(defn save-repo [user repo path content tokens]
  (let [commit-message "pinkgorilla notebook save"
        existing-file (load-repo-raw user repo path tokens)
        sha (:sha existing-file)
        _ (debug "existing git repo sha is: " sha)
        result (tentacles.repos/update-contents user repo path commit-message content sha tokens)]
    (trace "save response: " result)
    {:sha sha}
    (let [commit-message (get-in result [:commit :message])]
      (if commit-message
        {:success true :content (:content result)}
        {:success false :error-message (get-in result [:body :message])}))))

(comment

  (def creds {:oauth-token ""})
  (save-repo "pink-gorilla" "unittest-notebooks" "unittest.txt" "test!" creds)
  (load-repo "pink-gorilla" "unittest-notebooks" "unittest.txt" creds)
 ; 
  )

(extend-type StorageRepo
  Save
  (storage-save [self notebook tokens]
    (if (nil? tokens)
      {:success false :error-message "NOT Saving Notebook without token"}
      (if (nil? notebook)
        {:success false :error-message  "NOT Saving EMPTY Notebook"}
        (do
          (debug "Saving Notebook to repo: " (:repo self) " size: " (count notebook))
          (save-repo (:user self) (:repo self) (:filename self) notebook tokens)))))
  Load
  (storage-load [self tokens]
    (debug "Loading Notebook from repo: " (:repo self) "user: " (:user self) " filename: " (:filename self))
    (if (nil? tokens)
      (load-repo (:user self) (:repo self) (:filename self))
      (load-repo (:user self) (:repo self) (:filename self) tokens))))