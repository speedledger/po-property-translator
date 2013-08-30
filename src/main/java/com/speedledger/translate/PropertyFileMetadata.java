package com.speedledger.translate;

import com.speedledger.base.logging.shared.LoggerFactory;

import java.io.File;
import java.util.logging.Logger;

/**
 * Represents data about a xx.property-file
 */
public class PropertyFileMetadata {
    private static Logger LOG = LoggerFactory.getLogger(PropertyFileMetadata.class);
    private File file;

    public PropertyFileMetadata(File file) {
        this.file = file;
    }

    /**
     * get file the item refers to
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * get package to item refers to
     * @return the package-name
     */
    public String getPackage() {
        String packAndName = getPackageAndNameInternal();
        LOG.finest("packageName:" + packAndName);
        int len = packAndName.length() - getName().length();
        if (len == 0) {
            return "";
        }
        return packAndName.substring(0, len - 1);
    }

    /**
     * get package and name without file-extension.
     * @return package and name
     */
    public String getPackageAndNameWithoutExt() {
        return getPackage() + "/" + getNameNoExt();
    }

    private String getPackageAndNameInternal() {
        String name = file.getPath();
        String src = "resources";
        int i = name.indexOf(src);
        if (i < 0) {
            throw new IllegalArgumentException("can't find resource in:" + name);
        }
        return name.substring(i + src.length() + 1).replaceAll("\\\\", "/");
    }

    /**
     * get name of extension
     * @return name
     */
    public String getName() {
        return file.getName();
    }

    /**
     * get name without file-extension
     * @return name without extension
     */
    public String getNameNoExt() {
        String name = file.getName();
        return name.substring(0, name.indexOf(FileFinder.extension));
    }

    /**
     * return file with specified language
     *
     * @param lang language to use (or empty for default)
     * @return the language-file
     */
    public File getLangFile(String lang) {
        if (lang.length() > 0) {
            String path = file.getAbsolutePath();
            int cut = path.length() - FileFinder.extension.length();
            String cutPath = path.substring(0, cut);
            return new File(cutPath + "_" + lang + FileFinder.extension);
        } else {
            return file;
        }
    }

    @Override
    public String toString() {
        return getPackageAndNameInternal();
    }
}
