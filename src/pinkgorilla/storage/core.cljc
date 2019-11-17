(ns pinkgorilla.storage.core
  (:require

  ; dependencies needed to be in cljs bundle: 
   
    [pinkgorilla.storage.storage :as storage]   
   ;[pinkgorilla.storage.file]
   [pinkgorilla.storage.gist]
   ;[pinkgorilla.storage.repo]
   ;[pinkgorilla.storage.bitbucket]
   
   ;[pinkgorilla.storage.direct.file]
   ;[pinkgorilla.storage.direct.gist]
   ;[pinkgorilla.storage.direct.repo]
   ;[pinkgorilla.storage.direct.bitbucket]
   
   ))




(comment
  
  
  (storage/create-storage {:type :gist
                   :id "8204fd0b2aba27f06c04dffcb4fd0a24"})

  
  
  )
