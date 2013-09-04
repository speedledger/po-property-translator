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

To generate the `out.po` file (reads properties-files from current directory and subdirectories):
```shell
java -jar target/translate.jar export [language]
```
language specifies the export language, for example de, en, sv or left blank if the default language should be exported

To import back:
```shell
java -jar target/translate.jar import [language]
```

### Special features
* GWT map definitions will be excluded from the PO file, normally you do not want the keys of the map to be translated.
And their presents in the PO file might confuse the translator. Of course the values of the map will be present so that
they can be translated.

* Every term in the output will have the term in the default language present as a comment in the file. This is useful
if the file is watched in a text editor.

* If the default language file contains a comment starting with double hashes (\#\#). The comment will be added to
each term in the resulting PO file. This could be used as an extra context for the translator that could describe where
in the program the terms could be found.

### Example

An interface is created that extends googles i18n.Constans.
{code:title=FileMenuConstants.java|borderStyle=solid}
interface FileMenuConstants extends Constants {
    String openFile();
    String saveAs();
    String quit();
    Map<String,String> allItems();
}
{code}

Matching properties files are created with the tranlations (or stub of the translations).
{panel}
FileMenuConstants.properties          //default language (containing terms in english)
FileMenuConstants_sv.properties       //swedish
FileMenuConstants_de.properties       //german
{panel}

Content of "FileMenuConstants.properties"
{code:title=FileMenuConstants.properties|borderStyle=solid}
##Menu items of the file menu
openFile=Open file
saveAs=Save as
quit=Exit program
allItems = openFile, saveAs, quit
{code}

Content of "FileMenuConstants_sv.properties"
{code:title=FileMenuConstants_sv.properties|borderStyle=solid}
#This comment will be ignored
openFile=Öppna fil
saveAs=Spara som
quit=Avsluta
allItems = openFile, saveAs, quit
{code}

Run the export for swedish:
```shell
java -jar target/translate.jar export sv
```

A PO file named "out_sv.po" is created:
{code:title=out_sv.po|borderStyle=solid}
# Menu items of the file menu
# Open file
msgctxt "path/to/the/property/file/FileMenuConstants"
msgid "openFile"
msgstr "Öppna fil"

# Menu items of the file menu
# Save as
msgctxt "path/to/the/property/file/FileMenuConstants"
msgid "saveAs"
msgstr "Spara som"

# Menu items of the file menu
# Exit program
msgctxt "path/to/the/property/file/FileMenuConstants"
msgid "quit"
msgstr "Avsluta"

```

Use this file to create or update translations in swedish. And then import back

To import back:
```shell
java -jar target/translate.jar import sv
```

The file FileMenuConstants_sv.properties will now contain the new translations.



