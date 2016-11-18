(ns geof.writers
  (:require [topojson.writer :as topo])

  (:import [java.util.zip GZIPOutputStream])

  (:require [clojure.java.io :as io])
  (:require [clojure.data.json :as json]))

(defn as-one
  [& datasets]
  {:type "FeatureCollection"
   :features (flatten (map :features datasets))})

(defn write-to-geojson
  [writer & datasets] 
  (json/write (apply as-one datasets) writer))

(defn write-to-topojson
  [writer & datasets] 
    (json/write 
      (binding [topo/*type* float]
        (apply topo/geo2topo datasets))
      writer))

(defn write-to-shp
  [output & datasets] nil)

(defn writer-for-1
  [output-file]
  (if (re-find #"^-" output-file)
    (io/output-stream System/out)
    (io/output-stream (io/file output-file))))

(defn writer-for-0
  [output-file]
  (if (re-find #"gz$" output-file)
    (io/writer (GZIPOutputStream. (writer-for-1 output-file)))
    (io/writer (writer-for-1 output-file))))

(defn writer-fn
  [output-file]
  (cond 
    (re-find #"topo.?json(.gz)?$" output-file) write-to-topojson
    (re-find #"json(.gz)?$" output-file) write-to-geojson
    (re-find #"shp$" output-file) write-to-shp
    (re-find #"zip$" output-file) write-to-shp
    :else nil))

(defn writer-for
  [output-file] 
  (if-let [fun (writer-fn output-file)]
    (fn [& datasets]
      (with-open [writer (writer-for-0 output-file)]
        (apply fun writer datasets)))))

