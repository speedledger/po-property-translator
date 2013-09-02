package com.speedledger.translate.item;

import com.speedledger.translate.item.CommentItem;

/**
 * Represents a special type of comment that is used as context for the translator.
 * Usually this comment describes how to find the texts contained in the properties file in the program.
 *
 * example:
 * <pre>## These error messages can be found by selecting Files menu -> open file -> try to load a corrupt file </pre>
 */
public class DescriptionCommentItem extends CommentItem {
    public DescriptionCommentItem(String line) {
        super(line.trim());
    }

    @Override
    public String getPropertiesFileFormatted() {
        return "## " + getItem();
    }
}
