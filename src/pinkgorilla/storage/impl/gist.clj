(ns pinkgorilla.storage.impl.gist
  (:require
   [clojure.string]
   [taoensso.timbre :refer [trace debug info error]]
   [tentacles.gists]
   [pinkgorilla.storage.protocols :refer [Save Load]])
  (:import
   [pinkgorilla.storage.gist StorageGist]))

; gist save error
#_{:reason-phrase "Unauthorized"
   :headers {"X-RateLimit-Limit" "60"
             "X-RateLimit-Remaining" "51"
             "X-RateLimit-Reset" "1618991624"
             "X-RateLimit-Used" "9"}
   :status 401
   :length 119
   :body {:message "Requires authentication"
          :documentation_url "https://docs.github.com/rest/reference/gists/#create-a-gist"}}

; gist save success
#_{:description "d"
   :node_id "MDQ6R2lzdGMzNDY2N2IzMzhhZGZhNTBlMWYxYzBiN2MwZmNhYjQ3"
   :public true
   :updated_at "2021-04-22T01:37:55Z", :comments_url "https://api.github.com/gists/c34667b338adfa50e1f1c0b7c0fcab47/comments"
   :id "c34667b338adfa50e1f1c0b7c0fcab47"
   :truncated false
   :url "https://api.github.com/gists/c34667b338adfa50e1f1c0b7c0fcab47"
   :files {:abs.clj {:filename "abs.clj", :type "text/plain"
                     :language "Clojure"
                     :raw_url "https://gist.githubusercontent.com/awb99/c34667b338adfa50e1f1c0b7c0fcab47/raw/d800886d9c86731ae5c4a62b0b77c437015e00d2/abs.clj"
                     :size 3, :truncated false
                     :content "123"}}
   :forks [], :git_push_url "https://gist.github.com/c34667b338adfa50e1f1c0b7c0fcab47.git", :git_pull_url "https://gist.github.com/c34667b338adfa50e1f1c0b7c0fcab47.git", :user nil, :owner {:html_url "https://github.com/awb99", :gravatar_id "", :followers_url "https://api.github.com/users/awb99/followers", :subscriptions_url "https://api.github.com/users/awb99/subscriptions", :site_admin false, :following_url "https://api.github.com/users/awb99/following{/other_user}", :node_id "MDQ6VXNlcjEwODU0Njgy", :type "User", :received_events_url "https://api.github.com/users/awb99/received_events", :login "awb99", :organizations_url "https://api.github.com/users/awb99/orgs", :id 10854682, :events_url "https://api.github.com/users/awb99/events{/privacy}", :url "https://api.github.com/users/awb99", :repos_url "https://api.github.com/users/awb99/repos", :starred_url "https://api.github.com/users/awb99/starred{/owner}{/repo}", :gists_url "https://api.github.com/users/awb99/gists{/gist_id}", :avatar_url "https://avatars.githubusercontent.com/u/10854682?v=4"}, :created_at "2021-04-22T01:37:55Z"}
(defn extract [rep]
  (select-keys rep [:status
                    :length
                    :reason-phase
                    :body]))

(defn extract-gist-result [filename response]
  (trace "gist operation rep: " response) ; (extract response))
  (if (:id response)
    {:success true :filename filename :id (get-in response [:id])}
    {:success false :filename filename :id nil  :error-message (get-in response [:body :message])}))

(defn save-gist [id description is-public filename content tokens]
  (let [files {filename content}
        options (merge {:description (or description "")}
                       tokens)]
    (if (nil? id)
      (do (debug "creating gist: filename:" filename "opts: " options)
          (->>
           (tentacles.gists/create-gist files (assoc options :public is-public))
           (extract-gist-result filename)))
      (do (debug "updating gist: " id  " : " options)
          (->>
           (tentacles.gists/edit-gist id (assoc options :files {filename {:content content}}))
           (extract-gist-result filename))))))

(defn load-gist-all [gist-id & [tokens]]
  (tentacles.gists/file-contents
   (if (nil? tokens)
     (tentacles.gists/specific-gist gist-id)
     (tentacles.gists/specific-gist gist-id tokens))))

(defn load-gist [gist-id filename & [tokens]]
  (let [f (keyword filename)]
    (->> (load-gist-all gist-id tokens)
         (f))))

(extend-type StorageGist
  Save
  (storage-save [self notebook tokens]
    (if (nil? tokens)
      {:success false :error-message "NOT Saving Notebook without token"}
      (if (nil? notebook)
        {:success false :error-message "NOT Saving EMPTY Notebook"}
        (do
          (debug "Saving Notebook to gist: " (:filename self) " size:" (count notebook))
          (save-gist (:id self) (:description self) (:is-public self) (:filename self) notebook tokens)))))
  Load
  (storage-load [self tokens]
    (debug "Loading Notebook from gist id: " (:id self))
    (if (nil? tokens)
      (load-gist (:id self) (:filename self))
      (load-gist (:id self) (:filename self) tokens))))