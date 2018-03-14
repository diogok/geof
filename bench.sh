#!/bin/bash

#lein uberjar

FORMATS=("geojson" "mpack" "topojson" "topo.mpack")
COMPRESSES=(" " ".gz" ".xz" ".lz")

FOLDER="./test/data"
INPUT="$FOLDER/in.geojson"
DEST="$FOLDER"

JAVA_OPTS="-server -XX:+UseConcMarkSweepGC -XX:+UseCompressedOops -XX:+DoEscapeAnalysis -Xmx3G -Xms3G"

TARGET="$DEST/out.shp"

echo
echo "Target $TARGET"

time java $JAVA_OPTS -jar  \
  target/geof-0.0.1-standalone.jar "$TARGET" "$INPUT" 
du -sh $TARGET

for FORMAT in ${FORMATS[@]}
do
  TARGET="$DEST/out.$FORMAT"
  echo
  echo "Target $TARGET"

  time java $JAVA_OPTS -jar \
    target/geof-0.0.1-standalone.jar \
    $TARGET \
    $INPUT 

  du -sh $TARGET

  for COMPRESS in ${COMPRESSES[@]}
  do
    TARGET="$DEST/out.$FORMAT$COMPRESS"
    echo
    echo "Target $TARGET"

    time java $JAVA_OPTS -jar \
      target/geof-0.0.1-standalone.jar \
      $TARGET \
      $INPUT

    du -sh $TARGET
  done
done

ls -lah $DEST
