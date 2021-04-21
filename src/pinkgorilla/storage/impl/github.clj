(ns pinkgorilla.storage.impl.github
  (:require
   [clojure.string]
   [taoensso.timbre :refer [debug info error]]
   [tentacles.gists]
   [tentacles.repos]))



  ; :reason-phrase "Unauthorized"
  ;:status 401


(defn extract-gist-result [response]
  (info "gist operation rep: " response)
  (if (:id response)
    {:success true :id (:id response)}
    {:success false :error-message (get-in response [:body :message])}))

(defn save-gist [id description is-public filename content tokens]
  (let [files {filename content}
        options (merge {:description (or description "")}
                       tokens)]
    (if (nil? id)
      (do (info "creating gist: filename:" filename "opts: " options)
          (->>
           (tentacles.gists/create-gist files (assoc options :public is-public))
           (extract-gist-result)))
      (do (info "updating gist: " id  " : " options)
          (->>
           (tentacles.gists/edit-gist id (assoc options :files {filename {:content content}}))
           (extract-gist-result))))
    {:id id :filename filename}))

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


(defn save-repo [user repo path content tokens]
  (let [commit-message "pinkgorilla notebook save"
        existing-file (load-repo-raw user repo path tokens)
        sha (:sha existing-file)
        _ (info "existing git repo sha is: " sha)
        result (tentacles.repos/update-contents user repo path commit-message content sha tokens)]
    (debug "save response: " result)
    {:sha sha}))

(comment

  (def creds {:oauth-token ""})
  (save-repo "pink-gorilla" "unittest-notebooks" "unittest.txt" "test!" creds)
  (load-repo "pink-gorilla" "unittest-notebooks" "unittest.txt" creds)
 ; 
  )

