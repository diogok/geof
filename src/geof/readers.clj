(ns geof.readers
  (:require [topojson.reader :as topo])

  (:require [msgpack.core :as msg])
  (:require msgpack.clojure-extensions)

  (:import [java.util.zip GZIPInputStream])
  (:import [java.util HashMap])
  (:import [org.geotools.data DataStoreFinder])

  (:require [geof.geo :as geo])

  (:require [clojure.java.io :as io])
  (:require [clojure.data.json :as json]))

(defn read-geojson
  [reader] 
  [(json/read (io/reader reader) :key-fn keyword)])

(defn read-topojson
  [reader] 
  (-> (io/reader reader)
      (json/read :key-fn keyword)
      (topo/topo2geo)
      (get :features)))

(defn read-geo-msg-pack
  [reader]
  [(msg/unpack reader)])

(defn read-topo-msg-pack
  [reader]
  (-> reader
      (msg/unpack :key-fn keyword)
      (topo/topo2geo)
      (get :features)
      (doall)))

(defn read-shp
  [reader] 
    (let [cn (doto (HashMap.) 
               (.put "url" (.toURL reader)))
          ds (DataStoreFinder/getDataStore cn)
          tp (first (.getTypeNames ds))
          sc (.getFeatureSource ds tp)
          fc (.getFeatures sc)
          rr (geo/from-feat-collection fc)]
      (.dispose ds)
      [rr]))

(defn reader-for-1 
  [input-file]
  (cond 
    (re-find #"^-" input-file) (io/input-stream System/in)
    (re-find #"shp$" input-file) (io/file input-file)
    :else (io/input-stream (io/file input-file))))

(defn reader-for-0
  [input-file]
  (if (re-find #"gz$" input-file)
    (GZIPInputStream. (reader-for-1 input-file))
    (reader-for-1 input-file)))

(defn reader-fn
  [input-file]
  (cond 
    (re-find #"topo.?json(.gz)?$" input-file) read-topojson
    (re-find #"topo.?m(sg)?pack(.gz)?$" input-file) read-topo-msg-pack
    (re-find #"json(.gz)?$" input-file) read-geojson
    (re-find #"m(sg)?pack(.gz)?$" input-file) read-geo-msg-pack
    :else read-shp))

(defn reader-for
  [input-file] 
  (if-let [fun (reader-fn input-file)]
    (fn []
      (if (= read-shp fun)
        (fun (reader-for-0 input-file))
        (with-open [reader (reader-for-0 input-file)]
          (fun reader))))))

(defn read-from
  [input-file]
  ((reader-for input-file)))

