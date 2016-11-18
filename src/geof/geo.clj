(ns geof.geo
  (:import [org.geotools.feature.simple SimpleFeatureBuilder SimpleFeatureTypeBuilder]
           [org.geotools.data DataUtilities])
  (:require [cljts.relation :as rel]
            [cljts.geom :as geom]))

(defn coords2coords
  [pairs]
    (map 
      (fn [pair] 
        (geom/c (second pair) (first pair)))
      pairs))

(defn make-point
  [coords]
  (geom/point (geom/c (second coords) (first coords))))

(defn make-multi-point
  [coords]
  (geom/multi-point (map make-point coords)))

(defn make-line-string
  [coords]
  (geom/line-string 
    (map coords2coords coords)))

(defn make-poly
  [coords] 
  (let [cpoly (coords2coords (first coords))
        choles (map coords2coords (rest coords))]
    (geom/polygon
     (geom/linear-ring cpoly)
     (map geom/linear-ring choles))))

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

(defn make-feat
  [obj]
  (let [geom (make-geom obj)
        b0   (SimpleFeatureTypeBuilder.)]
    (.setName b0 (str (:type (:geometry obj)) (hash (map key (:properties obj)))))
    (doseq [kv (:properties obj)]
      (.add b0 (name (key kv)) (class (val kv))))
    (.add b0 "the_geom" (class geom))
    (let [feat-type (.buildFeatureType b0)
          b1 (SimpleFeatureBuilder. feat-type)]
      (doseq [kv (:properties obj)]
        (.add b1 (val kv)))
      (.add b1 geom)
      (.buildFeature b1 (:id obj)))))

(defn make-feat-collection
  [coll]
  (DataUtilities/collection (map make-feat (:features coll))))

