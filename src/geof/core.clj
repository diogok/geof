(ns geof.core 
  (:use geof.writers)
  (:use geof.readers)

  (:require [clojure.java.io :as io])
  (:require [clojure.data.json :as json])
  (:require [taoensso.timbre :as log])
  
  (:gen-class))

(defn -main
  [ & args ]
  (let [output-file (first args)
        input-files (rest args)
        writer (writer-for output-file)]
    (if (nil? writer)
      (do
        (log/error "Unkown output format" output-file)
        #_(System/exit 1))
      (do
        (doseq [input-file input-files]
          (if (nil? (reader-for input-file))
            (do 
              (log/error "Unkown input format" input-file)
              #_(System/exit 1))))
        (apply writer
          (flatten (map read-from input-files)))))))


