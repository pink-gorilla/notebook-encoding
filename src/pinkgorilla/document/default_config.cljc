(ns pinkgorilla.document.default-config
  (:require

    ; encoding  string <-> notebook
   [pinkgorilla.encoding.decode]  ; gorilla format decoding
   [pinkgorilla.encoding.encode] ; gorilla format encoding
   [pinkgorilla.encoding.jupyter] ; ipynb jupyter notebook    
   #?(:clj [pinkgorilla.encoding.marginalia]) ; clj with comments and output annotations
   #?(:clj [pinkgorilla.encoding.clj]) ; clj without comments

   ; storage string <-> storage
   [pinkgorilla.storage.file]
   #?(:clj [pinkgorilla.storage.gist])
   #?(:clj [pinkgorilla.storage.repo])
   [pinkgorilla.storage.bitbucket]
   #?(:clj [pinkgorilla.storage.github])

   ;[pinkgorilla.storage.direct.file]
   ;[pinkgorilla.storage.direct.gist]
   ;[pinkgorilla.storage.direct.repo]
   ;[pinkgorilla.storage.direct.bitbucket]
   ))