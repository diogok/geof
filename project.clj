(defproject geof "0.0.1"
  :description ""
  :url "https://github.com/diogok/geof"
  :license {:name "MIT"}
  :main geof.core
  :dependencies [[org.clojure/clojure "1.8.0"]

                 [org.clojars.diogok/cljts "0.5.1"]
                 [topojson "0.0.2"]

                 [org.clojure/data.json "0.2.6"]
                 [clojure-msgpack "1.2.0"]
                 [org.tukaani/xz "1.6"]

                 [com.taoensso/timbre "4.1.4"]]
  :repositories [["osgeo" "http://download.osgeo.org/webdav/geotools/"]
                 ["clojars" {:sign-releases false}]])
