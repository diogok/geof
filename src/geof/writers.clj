(ns geof.writers
  (:require [topojson.writer :as topo])

  (:require [msgpack.core :as msg])
  (:require msgpack.clojure-extensions)

  (:import [java.util.zip GZIPOutputStream])
  (:import [org.geotools.data.shapefile ShapefileDumper])

  (:require [geof.geo :as geo])

  (:require [clojure.java.io :as io])
  (:require [clojure.data.json :as json]))

(defn as-one
  [& datasets]
  {:type "FeatureCollection"
   :features (doall (flatten (map :features datasets)) )})

(defn write-to-geojson
  [writer & datasets] 
  (spit (io/writer writer)
        (json/write-str (apply as-one datasets))))

(defn write-to-topojson
  [writer & datasets] 
  (spit (io/writer writer)
    (json/write-str
      (binding [topo/*type* float]
        (apply topo/geo2topo datasets)))))

(defn write-to-topo-msg-pack
  [writer & datasets]
  (msg/pack-stream
    (binding [topo/*type* float]
        (apply topo/geo2topo datasets))
      writer))

(defn write-to-geo-msg-pack
  [writer & datasets]
  (msg/pack-stream (apply as-one datasets) writer))

(defn write-to-shp
  [output & datasets] 
  (let [all-feats (:features (apply as-one datasets))
        by-type (group-by #(get-in % [:geometry :type]) all-feats)]
    (if-not (.exists output) (.mkdir output))
    (doseq [f-type by-type]
      (let [re-output (io/file (str (.getAbsolutePath output ) "/" (key f-type) ))]
        (if-not (.exists re-output) (.mkdir re-output))
        (let [dumper (ShapefileDumper. re-output)]
          (.dump dumper
            (geo/make-feat-collection {:features (val f-type)})))))))

(defn writer-for-1
  [output-file]
  (cond 
    (re-find #"^-" output-file) (io/output-stream System/out)
    (re-find #"shp$" output-file) (io/file output-file)
    :else (io/output-stream (io/file output-file))))

(defn writer-for-0
  [output-file]
  (if (re-find #"gz$" output-file)
    (GZIPOutputStream. (writer-for-1 output-file))
    (writer-for-1 output-file)))

(defn writer-fn
  [output-file]
  (cond 
    (re-find #"topo.?json(.gz)?$" output-file) write-to-topojson
    (re-find #"topo.?m(sg)?pack(.gz)?$" output-file) write-to-topo-msg-pack
    (re-find #"json(.gz)?$" output-file) write-to-geojson
    (re-find #"m(sg)?pack(.gz)?$" output-file) write-to-geo-msg-pack
    :else write-to-shp))

(defn writer-for
  [output-file] 
  (if-let [fun (writer-fn output-file)]
    (fn [& datasets]
      (if (= write-to-shp fun)
        (apply fun (writer-for-0 output-file) datasets)
        (with-open [writer (writer-for-0 output-file)]
          (apply fun writer datasets)
          (.flush writer))))))

(defn write-to
  [output-file datasets]
  (apply (writer-for output-file) datasets))

