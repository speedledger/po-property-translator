package com.speedledger.translate;

import com.speedledger.base.logging.shared.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class finds language-files to process
 */
public class FileFinder {
    private static Logger LOG = LoggerFactory.getLogger(FileFinder.class);
    public static String extension = ".properties";
    private String[] pathStops = {".hg", "pom", "emulator", "test", "101", "conf", "classes", "logging"}; // ignore everything with this string anywhere.
    private String[] langStops = {"_de", "_no", "_sv"};

    private List<FileItem> processDir(File file, List<FileItem>files) {
        if (file.getName().endsWith(".properties") && allowed(file, langStops)) {
            LOG.finest("processDir file:" + file.getPath());
            files.add(new FileItem(file));
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
        return new FileList(processDir(rootDir, new ArrayList<FileItem>()));
    }
}
