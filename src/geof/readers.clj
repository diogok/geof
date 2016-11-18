(ns geof.readers
  (:require [topojson.reader :as topo])

  (:import [java.util.zip GZIPInputStream])

  (:require [clojure.java.io :as io])
  (:require [clojure.data.json :as json]))

(defn read-geojson
  [reader] 
  [(json/read reader :key-fn keyword)])

(defn read-topojson
  [reader] 
  (-> reader
      (json/read :key-fn keyword)
      (topo/topo2geo)
      (get :features)))

(defn read-shp
  [reader] nil)

(defn reader-for-1
  [input-file]
  (if (re-find #"^-" input-file)
    (io/input-stream System/in)
    (io/input-stream (io/file input-file))))

(defn reader-for-0
  [input-file]
  (if (re-find #"gz$" input-file)
    (io/reader (GZIPInputStream. (reader-for-1 input-file)))
    (io/reader (reader-for-1 input-file))))

(defn reader-fn
  [input-file]
  (cond 
    (re-find #"topo.?json(.gz)?$" input-file) read-topojson
    (re-find #"json(.gz)?$" input-file) read-geojson
    (re-find #"shp$" input-file) read-shp
    (re-find #"zip$" input-file) read-shp
    :else nil))

(defn reader-for
  [input-file] 
  (if-let [fun (reader-fn input-file)]
    (fn []
      (with-open [reader (reader-for-0 input-file)]
        (fun reader)))))

(defn read-from
  [input-file]
  ((reader-for input-file)))

