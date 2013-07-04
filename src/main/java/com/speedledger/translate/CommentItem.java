package com.speedledger.translate;

/**
 * This class represents a comment in a java-property file.
 */
public class CommentItem extends JavaProperty {
    String comment;

    public CommentItem(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CommentItem{" +
                "comment='" + comment + '\'' +
                '}';
    }

    @Override
    public String getItem() {
        return comment;
    }
}
