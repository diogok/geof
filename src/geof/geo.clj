(ns geof.geo
  (:import [org.geotools.feature.simple SimpleFeatureBuilder SimpleFeatureTypeBuilder]
           [org.geotools.data DataUtilities])
  (:require [clojure.data.json :as json])
  (:require [cljts.relation :as rel]
            [cljts.geom :as geom]
            [cljts.io :as gio]))

(defn coords2coords
  [pairs]
    (mapv
      (fn [pair] 
        (geom/c (first pair) (second pair)))
      pairs))

(defn make-point
  [coords]
  (geom/point (geom/c (second coords) (first coords))))

(defn make-multi-point
  [coords]
  (geom/multi-point (mapv make-point coords)))

(defn make-line-string
  [coords]
  (geom/line-string 
    (mapv coords2coords coords)))

(defn linear-ring
  [coords]
   (geom/linear-ring
     (if (>= (count coords) 4)
         coords
         [])))

(defn make-poly
  [coords] 
  (let [cpoly (coords2coords (first coords))
        choles (mapv coords2coords (rest coords))]
    (geom/polygon
     (linear-ring (conj cpoly (first cpoly)))
     (map linear-ring choles))))

(defn make-mpoly
  [coords] 
  (geom/multi-polygon (map make-poly coords)))

(defn make-geom
  [obj] 
  (let [t (:type (:geometry obj))
        c (:coordinates (:geometry obj))]
  (condp = t
    "Point" (make-point c)
    "MultiPoint" (make-multi-point c)
    "LineString" (make-line-string c)
    "Polygon" (make-poly c)
    "MultiPolygon" (make-mpoly c)
    nil
    )))

(defn make-type
  [ & objs] 
    (let [geom  (make-geom (first objs))
          b0    (SimpleFeatureTypeBuilder.)
          props (reduce merge {} (map :properties objs))]
      (.setName b0 (str (:type (:geometry (first objs))) (hash (map key props))))
      (doseq [kv props]
        (if (nil? (val kv))
          (.add b0 (name (key kv)) (class ""))
          (.add b0 (name (key kv)) (class (val kv)))))
      (.add b0 "the_geom" (class geom))
      (.buildFeatureType b0)))

(defn make-feat
  ([obj] 
    (let [geom  (make-geom obj)
          b0    (SimpleFeatureTypeBuilder.)
          props (:properties obj)]
      (.setName b0 (str (:type (:geometry obj)) (hash (map key props))))
      (doseq [kv props]
        (if-not (nil? (val kv))
          (.add b0 (name (key kv)) (class (val kv)))))
      (.add b0 "the_geom" (class geom))
      (make-feat (.buildFeatureType b0) obj)))
  ([feat-type obj]
    (let [b0 (SimpleFeatureBuilder. feat-type)
          geom (make-geom obj)]
      (doseq [kv (:properties obj)]
        (if-not (nil? (val kv))
          (.set b0 (name (key kv)) (val kv))))
      (.set b0 "the_geom" geom)
      (.buildFeature b0 (str (rand-int 999999))))))

(defn make-feat-collection
  [coll]
  (DataUtilities/collection 
    (map 
      (partial
        make-feat
         (apply make-type (:features coll))) 
      (:features coll))))

(defn from-geom
  [feat]
  (json/read-str 
    (gio/write-geojson (.getDefaultGeometry feat))
    :key-fn keyword))

(defn from-feat
  [feat] 
  {:id (.getID feat)
   :properties 
     (reduce merge {}
       (for [prop (.getProperties feat)]
         (when (and (not (nil? (.getValue prop)))
                    (not (= (.getValue prop) (.getDefaultGeometry feat))))
           {(keyword (.getLocalPart (.getName prop))) 
            (.getValue prop)})))
   :geometry (from-geom feat)})

(defn from-feat-collection
  [feat-col]
  {:type "FeatureCollection"
   :features 
     (let [iter (.features feat-col)
           feats (transient [])]
       (while (.hasNext iter)
         (conj! feats (from-feat (.next iter))))
       (.close iter)
       (persistent! feats))})

