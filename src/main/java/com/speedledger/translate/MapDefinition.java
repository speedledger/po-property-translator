package com.speedledger.translate;

/**
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
}
