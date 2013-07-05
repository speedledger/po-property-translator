po-property-translator
======================

Round-trip transformation between Java property files and PO-files for translations. 
This tool makes translating text inside java:s properties-files easier.
When running the export-tool, a .po-file is produces. This can be sent for translation.
When the file comes back, the tool PoImporter can import these texts back into the .properties-files.
It can be useful when using GWT (Google web toolkit).

Example:

Compile the tool:
mvn clean install

run:

to generate the out.po-file (reads properties-files from current directory):
java -jar target/translate.jar export

to import back:
java -jar target/translate.jar import
