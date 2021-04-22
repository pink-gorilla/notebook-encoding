(ns pinkgorilla.storage.impl.github
  (:require
   [clojure.string]
   [taoensso.timbre :refer [trace debug info error]]
   [tentacles.gists]
   [tentacles.repos]))

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
#_{:description "d",
   :node_id "MDQ6R2lzdGMzNDY2N2IzMzhhZGZhNTBlMWYxYzBiN2MwZmNhYjQ3",
   :public true,
   :updated_at "2021-04-22T01:37:55Z", :comments_url "https://api.github.com/gists/c34667b338adfa50e1f1c0b7c0fcab47/comments",
   :id "c34667b338adfa50e1f1c0b7c0fcab47",
   :truncated false,
   :url "https://api.github.com/gists/c34667b338adfa50e1f1c0b7c0fcab47",
   :files {:abs.clj {:filename "abs.clj", :type "text/plain",
                     :language "Clojure",
                     :raw_url "https://gist.githubusercontent.com/awb99/c34667b338adfa50e1f1c0b7c0fcab47/raw/d800886d9c86731ae5c4a62b0b77c437015e00d2/abs.clj",
                     :size 3, :truncated false,
                     :content "123"}},
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
      (do (info "creating gist: filename:" filename "opts: " options)
          (->>
           (tentacles.gists/create-gist files (assoc options :public is-public))
           (extract-gist-result filename)))
      (do (info "updating gist: " id  " : " options)
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


#_{:content {:path "bongo.txt",
             :name "bongo.txt",
             :type "file",
             :size 8,
             :sha "0839b2e9412b314cb8bb9a20f587aa13752ae310"},
   :commit {:tree {:sha "328d0c1bf3bc3c49f53a9c13270db1d6d6590b4d",
                   :url "https://api.github.com/repos/pink-junkjard/unittest-notebooks/git/trees/328d0c1bf3bc3c49f53a9c13270db1d6d6590b4d"},
            :committer {:name "awb99", :email "10854682+awb99@users.noreply.github.com",
                        :date "2021-04-22T01:20:56Z"},
            :node_id "MDY6Q29tbWl0MjE2MzM3MjExOmE2M2JlYjQ4Y2EwMWM5NzIzN2FlMzFiZWE3NDU5YmIxNDRiMTc0MTk=",
            :author {:name "awb99", :email "10854682+awb99@users.noreply.github.com", :date "2021-04-22T01:20:56Z"},
            :parents [{:sha "5ab0e21776682694b005716917c65876f43bfd01"}],
            :sha "a63beb48ca01c97237ae31bea7459bb144b17419",
            :message "pinkgorilla notebook save"}}

(defn save-repo [user repo path content tokens]
  (let [commit-message "pinkgorilla notebook save"
        existing-file (load-repo-raw user repo path tokens)
        sha (:sha existing-file)
        _ (info "existing git repo sha is: " sha)
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

