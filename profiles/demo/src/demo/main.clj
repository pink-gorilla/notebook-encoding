(ns demo.main
  (:require
   [taoensso.timbre :refer [debug info error]]
   [pinkgorilla.encoding.default-config] ; side-effects
   [pinkgorilla.storage.impl.gist :refer [save-gist]]
    [pinkgorilla.storage.impl.repo :refer [save-repo]]
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.persistence :refer [load-notebook]]
   )
  (:gen-class))


(defn raw [tokens]
   (let [
         id nil
         description "d"
         is-public true
         filename "abs.clj"
         content "123"]
     (info "gist-result: " (save-gist id description is-public filename content tokens))
     (info "repo-result: " (save-repo "pink-junkjard" "unittest-notebooks" "bongo.txt" "contents" tokens))

    ; error
     (error "gist-result: " (save-gist id description is-public filename content {}))
     (error "repo-result: " (save-repo "pink-junkjard" "bad-unittest-notebooks" "bongo.txt" "contents" tokens)))
  )


(defn -main []
  (let [tokens (creds)
        ;tokens {:oauth-token ""}
        
        s (create-storage  {:user "pink-gorilla", :repo "gorilla-ui", :filename "notebooks/vega.cljg", :type :repo}
                           #_{:type :res :filename "bongo.cljg"})]
    
    ;(raw tokens)
    (info (load-notebook s tokens))
    
    )
 )



