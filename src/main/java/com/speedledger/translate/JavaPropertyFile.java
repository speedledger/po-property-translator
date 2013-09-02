package com.speedledger.translate;

import com.google.common.collect.Maps;
import com.speedledger.translate.item.PropertyFileItem;
import com.speedledger.translate.item.TranslatableItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class represents a java-property-file. Lines keep their order.
 */
public class JavaPropertyFile {
    private List<PropertyFileItem> content = new ArrayList<PropertyFileItem>();
    private Map<String, TranslatableItem> propertiesByKey = Maps.newHashMap();

    public List<PropertyFileItem> getContent() {
        return Collections.unmodifiableList(content);
    }

    public void addItem(PropertyFileItem propertyFileItem) {
        content.add(propertyFileItem);
        putInMap(propertyFileItem);
    }

    private void putInMap(PropertyFileItem propertyFileItem) {
        if (propertyFileItem instanceof TranslatableItem) {
            TranslatableItem translatableItem = (TranslatableItem) propertyFileItem;
            propertiesByKey.put(translatableItem.getKey(), translatableItem);
        }
    }

    public TranslatableItem getItem(String key) {
        return propertiesByKey.get(key);
    }

    public void setItem(int index, PropertyFileItem propertyFileItem) {
        PropertyFileItem removedItem = content.set(index, propertyFileItem);
        if(removedItem != null && removedItem instanceof TranslatableItem) {
            propertiesByKey.remove(((TranslatableItem) removedItem).getKey());
        }
        putInMap(propertyFileItem);
    }
}
