package com.speedledger.translate;

/**
 * base class for rows in a java property-file.
 */
public abstract class PropertyFileItem {
    /**
      * @return String representing this line in the property-file.
     */
    public abstract String getItem();

    public String getPropertiesFileFormatted() {
        return getItem();
    }

}
