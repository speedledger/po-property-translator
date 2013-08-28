package com.speedledger.translate;

import com.speedledger.base.logging.shared.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * class for reading java-property-files.
 */
public class JavaPropertyFileReader {
    private static Logger LOG = LoggerFactory.getLogger(JavaPropertyFileReader.class);

    public static JavaPropertyFile readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        Parser parser = new Parser();
        String parseLine = "";
        while ((line = reader.readLine()) != null) {
            if (line.endsWith("\\")) { // buffer to-be-followed-line
                parseLine += line.substring(0, line.length() - 1);
            } else { // handle line
                parseLine += line;
                parser.parseRow(parseLine, file);
                parseLine = "";
            }
        }
        reader.close();
        parser.cleanUpMapDefinitions();
        return parser.parsedData;
    }

    private static class Parser {
        JavaPropertyFile parsedData = new JavaPropertyFile();

        public void parseRow(String line, File file) {
            if (isEmptyLine(line)) {
                parsedData.addItem(new EmptyLineItem());
            } else if (isDescriptionComment(line)) {
                parsedData.addItem(new DescriptionCommentItem(line.substring(2)));
            } else if (isComment(line)) { // comment found
                parsedData.addItem(new CommentItem(line.substring(1)));
            } else {
                LOG.finest("read line:" + line);
                String[] item = line.split("=");
                String value = "";
                if (item.length == 2) {
                    value = item[1];
                }
                if (item.length == 0 || item.length > 2) {
                    LOG.warning("warning, invalid line, many'=' in line:" + line + " file:" + file);
                    value = line.substring(item[0].length() + 1); // treat as translatable-item
                }
                parsedData.addItem(new TranslatableItem(item[0], value));
            }
        }

        /**
         * Remove GWT map definitions from the translation output.
         */
        public void cleanUpMapDefinitions() {
            for (int i = 0; i < parsedData.getContent().size(); i++) {
                PropertyFileItem javaProperty = parsedData.getContent().get(i);
                if (javaProperty instanceof TranslatableItem) {
                    TranslatableItem translatableItem = (TranslatableItem) javaProperty;
                    if (isMapDefinition(translatableItem)) {
                        parsedData.setItem(i, new MapDefinition(translatableItem));
                    }
                }
            }
        }

        /**
         * Returns true if the translatable item represent a GWT map definition.
         * @param translatableItem a translatable item that should be examined if it represents a map definition
         * @return true if the item is a map definition
         */
        private boolean isMapDefinition(TranslatableItem translatableItem) {
            for (String key : translatableItem.getValue().split(",")) {
                if (parsedData.getItem(key.trim()) == null) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean isEmptyLine(String line) {
        return line.trim().isEmpty();
    }

    private static boolean isDescriptionComment(String line) {
        return line.startsWith("##");
    }

    private static boolean isComment(String line) {
        return line.startsWith("#");
    }

}
