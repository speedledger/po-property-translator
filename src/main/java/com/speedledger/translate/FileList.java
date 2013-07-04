package com.speedledger.translate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This immutable class holds a list of files for processing
 */
public class FileList {
    private List<FileItem> files;
    private Map<String, FileItem> itemByPackage;

    public FileList(List<FileItem>files) {
        this.files=files;
        itemByPackage = new HashMap<String, FileItem>();
        for (FileItem item : files) {
            itemByPackage.put(item.getPackageAndNameWithoutExt(), item);
        }
    }

    public List<FileItem> getFiles() {
        return files;
    }

    public FileItem getItemByPackage(String name) {
        return itemByPackage.get(name);
    }

}
