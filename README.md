po-property-translator
======================

Round-trip transformation between Java property files and PO-files for translations. 
This tool makes translating text inside Java's property files easier.
When running the export tool, a `.po`-file is produced. This can be sent for translation.
When the file comes back, the tool `PoImporter` can import these texts back into the property files.

We use it in conjunction with GWT (Google Web Toolkit).

### Usage

Compile using Maven:
```shell
  mvn clean install
```

To generate the `out.po` file (reads properties-files from current directory):
```shell
java -jar target/translate.jar export
```

To import back:
```shell
java -jar target/translate.jar import
```
