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
| GeoMsgPack | xz | 1.7MB |  15s |
| TopoJSON | - | 524KB | 22s |
| TopoJSON | gz | 84KB | 20s |
| TopoJSON | xz | 64KB | 22s |
| TopoMsgPack | - | 256KB | 21s |
| TopoMsgPack | gz | 76KB | 20s |
| TopoMsgPack | xz | 60KB | 22s |

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

If filename is "-" (ex.: javar -jar geof.jar -.json.gz -.shp) it will use stdin for input or stdout for output.

When converting to shapefile, beware of the limitation of only a single geometry type per shapefile, column name size, column content limit and such.

### As a library in Clojure

_SOON_

## License

MIT

