package com.speedledger.translate.item;

/**
 * This class handles Maps within a properties-file.
 */
public class MapDefinition extends PropertyFileItem {
    private final String value;
    private final String key;

    public MapDefinition(TranslatableItem translatableItem) {
        this.key = translatableItem.getKey();
        this.value = translatableItem.getValue();

    }

    @Override
    public String getItem() {
        return value;
    }

    public String getPropertiesFileFormatted() {
        return key + "=" + value;
    }
}
