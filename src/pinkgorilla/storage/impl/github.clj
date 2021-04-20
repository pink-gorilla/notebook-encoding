(ns pinkgorilla.storage.impl.github
  (:require
   [clojure.string]
   [taoensso.timbre :refer [debug info error]]
   [tentacles.gists]
   [tentacles.repos]))


;defn load-gist [gist-id & [token]]


  ; :reason-phrase "Unauthorized"
  ;:status 401


(defn extract-gist-result [response]
  (if (:id response)
    {:success true :id (:id response)}
    {:success false :error-message (get-in response [:body :message])}))

(defn save-gist [id description is-public filename content token]
  (let [files {filename content}
        options {:description description
                 :oauth-token token}]
    (if (nil? id)
      (do (info "creating gist: " options)
          (->>
           (tentacles.gists/create-gist files (assoc options :public is-public))
           (extract-gist-result)))
      (do (info "updating gist: " id  " : " options)
          (->>
           (tentacles.gists/edit-gist id (assoc options :files {filename {:content content}}))
           (extract-gist-result))))
    {:id id :filename filename}))

(defn load-gist-all [gist-id & [token]]
  (tentacles.gists/file-contents
   (if (nil? token)
     (tentacles.gists/specific-gist gist-id)
     (tentacles.gists/specific-gist gist-id {:oauth-token token}))))

(defn load-gist [gist-id filename & [token]]
  (let [f (keyword filename)]
    (->> (load-gist-all gist-id token)
         (f))))

(defn load-repo-raw [user repo path & [token]]
  (if (nil? token)
    (tentacles.repos/contents user repo path {:str? true})
    (tentacles.repos/contents user repo path {:str? true :oauth-token token})))

(defn load-repo [user repo path & [token]]
  (:content
   (if (or (nil? token) (clojure.string/blank? token))
     (tentacles.repos/contents user repo path {:str? true})
     (tentacles.repos/contents user repo path {:str? true :oauth-token token}))))


;; body :message


(defn save-repo [user repo path content token]
  (let [commit-message "pinkgorilla notebook save"
        existing-file (load-repo-raw user repo path token)
        sha (:sha existing-file)
        _ (info "existing git repo sha is: " sha)
        result (tentacles.repos/update-contents user repo path commit-message content sha {:oauth-token token})]
    (debug "save response: " result)
    {:sha sha}))

(comment

  (def creds {:github-token ""})

  (save-repo "pink-gorilla" "unittest-notebooks" "unittest.txt" "test!" (:github-token creds))

  (load-repo "pink-gorilla" "unittest-notebooks" "unittest.txt" (:github-token creds)))

