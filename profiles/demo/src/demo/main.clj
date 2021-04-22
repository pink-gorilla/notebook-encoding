(ns demo.main
  (:require
   [taoensso.timbre :refer [debug info error]]
   [pinkgorilla.storage.impl.github :refer [save-gist save-repo]]
   [pinkgorilla.creds :refer [creds]])
  (:gen-class))


(defn -main []
  (let [tokens (creds)
        ;tokens {:oauth-token ""}
        id nil
        description "d"
        is-public true
        filename "abs.clj"
        content "123"]
    (info "gist-result: " (save-gist id description is-public filename content tokens))
    (info "repo-result: " (save-repo "pink-junkjard" "unittest-notebooks" "bongo.txt" "contents" tokens))

    ; error
    (error "gist-result: " (save-gist id description is-public filename content {}))
    (error "repo-result: " (save-repo "pink-junkjard" "bad-unittest-notebooks" "bongo.txt" "contents" tokens))))



