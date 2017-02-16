# geof

A library and command line interface to convert between geospatial vector data formats:

- GeoJSON
- TopoJSON
- Shapefile

The [GeoJSON](http://geojson.org) and [TopoJSON](https://github.com/topojson/topojson) may actually be represented as [JSON](http://json.org) or [MessagePack](http://msgpack.org) and compressed with _gz_(DEFLATE) or _xz_ for minimum size.


## Comparisson

As with any benchmark try on your data and make your measurements since it varies a lot.

This is reading from a source geojson of 9MB.

| Format | Compress | Size | Time |
|------- |---------|------|------|
| Shapefile | - | 5MB  | - |
| Shapefile | zip | 3.2MB  | - |
| GeoJSON | - | 9MB | 10s  |
| GeoJSON | gz | 2.5MB | 11s |
| GeoJSON | xz | 1.4MB | 21s |
| GeoMsgPack | - | 5.2MB | 9s |
| GeoMsgPack | gz | 3.5MB | 11s |
| GeoMsgPack | xz | 1.7MB |  20s |
| TopoJSON | - | 7.2MB | 16s |
| TopoJSON | gz | 1.8MB | 17s |
| TopoJSON | xz | 1.1MB | 25s |
| TopoMsgPack | - | 4.2MB | 15s |
| TopoMsgPack | gz | 1.5MB | 17s |
| TopoMsgPack | xz | 754KB | 27s |

And using as a source [mapzend borders](https://mapzen.com/data/borders/) of Brazil cities, a 91MB geojson:

| Format | Compress | Size | Time |
|------- |---------|------|------|
| Shapefile | - | 206MB | 30s |
| Shapefile | zip | 33MB  | - |
| GeoJSON | - | 91MB | 33s  |
| GeoJSON | gz | 24MB | 37s |
| GeoJSON | xz | 13MB | 2m27s |
| GeoMsgPack | - | 54MB | 26s |
| GeoMsgPack | gz | 34MB | 36s |
| GeoMsgPack | xz | 12MB | 1m35s |
| TopoJSON | - | 72MB | 1m22s |
| TopoJSON | gz | 18MB | 1m26s |
| TopoJSON | xz | 10MB | 2m41s |
| TopoMsgPack | - | 43MB | 1m19s |
| TopoMsgPack | gz | 16MB | 1m44s |
| TopoMsgPack | xz | 6.6MB | 2m14s |

So choose your own trade-ofs.

## Usage

### Standalone CLI

You will need java installed.

Download the latest jar from the [realases page](https://github.com/diogok/geof/releases) and run it:

    $ java -jar geof.jar output.format input.format input.format input.format

Where output.format is the name of the file after conversion. Supporting the following extensions:

- output.shp to create individual shp files in folder output
- output.json to create a GeoJSON file
- output.mpack to create a GeoJSON in MsgPack binary file
- output.json.gz to create a compressed GeoJSON file
- output.json.xz to create a more compressed GeoJSON file
- output.mpack.gz to create a compressed GeoJSON in MsgPack binary file
- output.mpack.xz to create a more compressed GeoJSON in MsgPack binary file
- output.topo.json to create a TopoJSON file
- output.topo.mpack to create a TopoJSON in MsgPack binary file
- output.topo.json.gz to create a compressed TopoJSON file
- output.topo.json.xz to create a more compressed TopoJSON file
- output.topo.mpack.gz to create a compressed TopoJSON in MsgPack binary file
- output.topo.mpack.xz to create a more compressed TopoJSON in MsgPack binary file

If filename is "-" (ex.: javar -jar geof.jar -.json.gz -.shp) it will use standart in for input or standart out for output.

When converting to shapefile, beware of the limitation of only a single geometry type per shapefile, column name size, column content limit and such.

### As a library in Clojure

_SOON_

## License

MIT

