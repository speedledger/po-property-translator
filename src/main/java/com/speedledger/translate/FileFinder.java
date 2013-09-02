package com.speedledger.translate;

import com.speedledger.base.logging.shared.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class finds language-files to process
 */
public class FileFinder {
    private static Logger LOG = LoggerFactory.getLogger(FileFinder.class);
    public static String extension = ".properties";
    private static String[] pathStops; // ignore everything with these filenames.
    private static String[] langStops; // ignore everything with these filenames.
    static {
        String settingsFile="/settings.properties";
        InputStream fis = FileFinder.class.getResourceAsStream(settingsFile);
        Properties prop = new Properties();
        try {
            prop.load(fis);
            pathStops=prop.getProperty("pathStops").split(",");
            langStops=prop.getProperty("langStops").split(",");
        } catch (IOException e) {
            LOG.log(Level.SEVERE,"failed to read settings file:"+settingsFile, e);
        }
    }

    private List<PropertyFileMetadata> processDir(File file, List<PropertyFileMetadata>files) {
        if (file.getName().endsWith(".properties") && allowed(file, langStops)) {
            LOG.finest("processDir file:" + file.getPath());
            files.add(new PropertyFileMetadata(file));
        }
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                if (allowed(child, pathStops)) {
                    processDir(child, files);
                }
            }
        }
        return files;
    }

    private boolean allowed(File child, String[] stops) {
        for (String stop : stops) {
            if (child.getPath().contains(stop)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get a list of files to work with
     * @param rootDir the root directory where to search (recursively)
     * @return list of files found
     */
    public FileList getFileList(File rootDir) {
        return new FileList(processDir(rootDir, new ArrayList<PropertyFileMetadata>()));
    }
}
