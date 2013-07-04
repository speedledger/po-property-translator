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
        return parser.parsedData;
    }
    private static class Parser {
        JavaPropertyFile parsedData = new JavaPropertyFile();

        public void parseRow(String line, File file) {
            if (isComment(line)) { // comment found
                parsedData.getContent().add(new CommentItem(line));
            } else {
                LOG.finest("read line:" + line);
                String[] item = line.split("=");
                String value = "";
                if (item.length == 2) {
                    value = item[1];
                }
                if (item.length == 0 || item.length > 2) {
                    LOG.warning("warning, invalid line, many'=' in line:" + line + " file:"+file);
                    value = line.substring(item[0].length() + 1); // treat as translatable-item
                }
                parsedData.getContent().add(new TranslatableItem(item[0], value));
            }

        }
    }

    private static boolean isComment(String line) {
        if (line.length() == 0 || line.trim().charAt(0) == '#') {
            return true;
        }
        if (line.endsWith("\\")) {
            return true;
        }
        if (line.contains(",")) {
            return true;
        }
        return false;
    }

}
