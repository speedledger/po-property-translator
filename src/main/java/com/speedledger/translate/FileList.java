package com.speedledger.translate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This immutable class holds a list of files for processing
 */
public class FileList {
    private List<PropertyFileMetadata> files;
    private Map<String, PropertyFileMetadata> itemByPackage;

    public FileList(List<PropertyFileMetadata> files) {
        this.files = files;
        itemByPackage = new HashMap<String, PropertyFileMetadata>();
        for (PropertyFileMetadata item : files) {
            itemByPackage.put(item.getPackageAndNameWithoutExt(), item);
        }
    }

    public List<PropertyFileMetadata> getFiles() {
        return files;
    }

    public PropertyFileMetadata getItemByPackage(String name) {
        return itemByPackage.get(name);
    }

}
