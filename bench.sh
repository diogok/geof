#!/bin/bash

#lein uberjar

time java -server -XX:+UseConcMarkSweepGC -XX:+UseCompressedOops -XX:+DoEscapeAnalysis -jar -Xmx3G -Xms3G \
  target/geof-0.0.1-standalone.jar \
  admin_level_8.geojson \
  ~/Downloads/brazil/admin_level_8.geojson

time java -server -XX:+UseConcMarkSweepGC -XX:+UseCompressedOops -XX:+DoEscapeAnalysis -jar -Xmx3G -Xms3G \
  target/geof-0.0.1-standalone.jar \
  admin_level_8.geojson.gz \
  ~/Downloads/brazil/admin_level_8.geojson

time java -server -XX:+UseConcMarkSweepGC -XX:+UseCompressedOops -XX:+DoEscapeAnalysis -jar -Xmx3G -Xms3G \
  target/geof-0.0.1-standalone.jar \
  admin_level_8.topo.mpack \
  ~/Downloads/brazil/admin_level_8.geojson

time java -server -XX:+UseConcMarkSweepGC -XX:+UseCompressedOops -XX:+DoEscapeAnalysis -jar -Xmx3G -Xms3G \
  target/geof-0.0.1-standalone.jar \
  admin_level_8.topo.mpack.gz \
  ~/Downloads/brazil/admin_level_8.geojson

time java -server -XX:+UseConcMarkSweepGC -XX:+UseCompressedOops -XX:+DoEscapeAnalysis -jar -Xmx3G -Xms3G \
  target/geof-0.0.1-standalone.jar \
  admin_level_8.topo.mpack.xz \
  ~/Downloads/brazil/admin_level_8.geojson
