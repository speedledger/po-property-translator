package com.speedledger.translate;

/**
 * This class represents translatable item in a java-property file.
 */
public class TranslatableItem extends PropertyFileItem {
    private String key;
    private String value;

    @Override
    public String toString() {
        return "TranslatableItem{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public TranslatableItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getItem() {
        return key + "=" + value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
