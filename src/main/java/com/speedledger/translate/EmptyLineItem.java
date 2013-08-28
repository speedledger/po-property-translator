package com.speedledger.translate;

/**
 * Represents an empty line that should be preserved in a properties file.
 */
public class EmptyLineItem extends JavaProperty {
    @Override
    public String getItem() {
        return "";
    }
}
