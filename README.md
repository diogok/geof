# geof

A library and command line interface to convert between geospatial vector data formats:

- GeoJSON
- TopoJSON
- Shapefile

## Why?

Because a 100MB shp can turn into a 2MB Topology msg-pack compressed, but you will need the shapefile back for some programs.

## Usage

### Standalone CLI

You will need java installed.

Download the latest jar from the [realases page](https://github.com/diogok/geof/releases) and run it:

    $ java -jar geof.jar output.format input.format input.format input.format

Where output.format is the name of the file after conversion. Supporting the following extensions:

- output.shp to create individual shp files
- output.zip to create shp files in a zip archive
- output.json to create a GeoJSON file
- output.json.gz to create a compressed GeoJSON file
- output.topo.json to create a TopoJSON file
- output.topo.json.gz to create a compressed TopoJSON file (great for the web with minimal size)

If filename is "-" (ex.: javar -jar geof.jar -.json.gz -.shp) it will use standart in for input or standart out for output.

When converting to shapefile, beware of the limitation of only a single geometry type per shapefile.

### As a library in Clojure

_SOON_

## License

MIT

