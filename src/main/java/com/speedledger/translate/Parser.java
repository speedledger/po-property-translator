package com.speedledger.translate;

import com.speedledger.base.logging.shared.LoggerFactory;
import com.speedledger.translate.item.CommentItem;
import com.speedledger.translate.item.DescriptionCommentItem;
import com.speedledger.translate.item.EmptyLineItem;
import com.speedledger.translate.item.MapDefinition;
import com.speedledger.translate.item.PropertyFileItem;
import com.speedledger.translate.item.TranslatableItem;

import java.io.Reader;
import java.util.logging.Logger;

/**
 * This class parses a property-file.
 */
class Parser {
    private static Logger LOG = LoggerFactory.getLogger(Parser.class);
    private JavaPropertyFile parsedData = new JavaPropertyFile();

    public void parseRow(String line, Reader file) {
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

    public JavaPropertyFile getParsedData() {
        return parsedData;
    }

    /**
     * Returns true if the translatable item represent a GWT map definition.
     * @param translatableItem a translatable item that should be examined if it represents a map definition
     * @return true if the item is a map definition
     */
    private boolean isMapDefinition(TranslatableItem translatableItem) {
        int keysFound = 0;
        String[] keys = translatableItem.getValue().split(",");
        for (String key : keys) {
            if (parsedData.getItem(key.trim()) != null) {
                keysFound++;
            }
        }
        if(keysFound > 0) {
            if(keysFound < keys.length) {
                LOG.warning("Some keys are missing their definition in :"+translatableItem);
            }
            return true;
        }
        return false;
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
