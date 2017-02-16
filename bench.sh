#!/bin/bash

#lein uberjar

FORMATS=("geojson" "mpack" "topojson" "topo.mpack")
COMPRESSES=(" " ".gz" ".xz")

INPUT="test/data/in.geojson"
DEST="test/data"

JAVA_OPTS="-server -XX:+UseConcMarkSweepGC -XX:+UseCompressedOops -XX:+DoEscapeAnalysis -Xmx3G -Xms3G"

TARGET="$DEST/out.shp"
time java $JAVA_OPTS -jar  \
  target/geof-0.0.1-standalone.jar "$TARGET" "$INPUT"
du -sh $TARGET

for FORMAT in ${FORMATS[@]}
do
  TARGET="$DEST/out.$FORMAT"
  echo "Target $TARGET"

  time java $JAVA_OPTS -jar \
    target/geof-0.0.1-standalone.jar \
    $TARGET \
    $INPUT

  ls -lah $TARGET

  for COMPRESS in ${COMPRESSES[@]}
  do
    TARGET="$DEST/out.$FORMAT$COMPRESS"
    echo "Target $TARGET"

    time java $JAVA_OPTS -jar \
      target/geof-0.0.1-standalone.jar \
      $TARGET \
      $INPUT

    ls -lah $TARGET
  done
done

ls -lah $DST
