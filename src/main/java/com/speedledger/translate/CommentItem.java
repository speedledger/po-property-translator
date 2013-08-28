package com.speedledger.translate;

/**
 * This class represents a comment in a java-property file.
 */
public class CommentItem extends JavaProperty {
    String rawComment;

    public CommentItem(String rawComment) {
        this.rawComment = rawComment;
    }

    @Override
    public String toString() {
        return "CommentItem{" +
                "comment='" + rawComment + '\'' +
                '}';
    }

    @Override
    public String getItem() {
        return rawComment;
    }

    @Override
    public String getPropertiesFileFormatted() {
        return "#" + getItem();
    }
}
