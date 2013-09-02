package com.speedledger.translate.item;

import com.speedledger.translate.item.PropertyFileItem;

/**
 * Represents an empty line that should be preserved in a properties file.
 */
public class EmptyLineItem extends PropertyFileItem {
    @Override
    public String getItem() {
        return "";
    }
}
