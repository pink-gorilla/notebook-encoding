(ns pinkgorilla.storage.github
  (:require
   [clojure.tools.logging :refer (info)]
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
      (do (println "creating gist: " options)
          (->>
           (tentacles.gists/create-gist files (assoc options :public is-public))
           (extract-gist-result)))
      (do (println "updating gist: " id  " : " options)
          (->>
           (tentacles.gists/edit-gist id (assoc options :files {filename {:content content}}))
           (extract-gist-result))))))


(defn load-gist [gist-id & [token]]
  (tentacles.gists/file-contents
   (if (nil? token)
     (tentacles.gists/specific-gist gist-id)
     (tentacles.gists/specific-gist gist-id {:oauth-token token}))))




(defn load-repo [user repo path & [token]]
   (if (nil? token)
     (tentacles.repos/contents user repo path)
     (tentacles.repos/contents user repo path {:str? true :oauth-token token})))


;; body :message

(defn save-repo [user repo path content token]
  (let [existing-file (load-repo user repo path token)
        sha (:sha existing-file)
        _ (println "sha is: " sha)]
  (tentacles.repos/update-contents user repo path "pinkgorilla notebook save" content sha {:oauth-token token})
  ))


(comment
  (def creds
    (-> (slurp "/tmp/creds.edn")
        (clojure.edn/read-string)))

  (save-repo "pink-gorilla" "sample-notebooks" "unittest.txt" "test!" (:github creds))
   
 (load-repo "pink-gorilla" "sample-notebooks" "unittest.txt" (:github creds))
  
  )

 