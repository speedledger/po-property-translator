package com.speedledger.translate;

import com.speedledger.base.logging.shared.LoggerFactory;
import com.speedledger.translate.item.PropertyFileItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Methods for performing disk IO
 */
public class IO {

    private static Logger log = LoggerFactory.getLogger(IO.class);
    private String defaultFileName = "out_[lang].po";

    public String getDefaultPOFileName(String lang) {
        return defaultFileName.replace("[lang]", lang);
    }

    public JavaPropertyFile readJavaPropertyFile(File file) throws IOException {
        return readJavaPropertyFile(new FileReader(file));
    }

    public JavaPropertyFile readJavaPropertyFile(Reader fileReader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        Parser parser = new Parser();
        String parseLine = "";
        while ((line = bufferedReader.readLine()) != null) {
            line=convertToInternal(line);
            if (line.endsWith("\\")) { // buffer to-be-followed-line
                parseLine += line.substring(0, line.length() - 1);
            } else { // handle line
                parseLine += line;
                parser.parseRow(parseLine, bufferedReader);
                parseLine = "";
            }
        }
        bufferedReader.close();
        parser.cleanUpMapDefinitions();
        return parser.getParsedData();
    }

    private String convertToInternal(String line) {
        return line.replaceAll("''","'");
    }

    private String convertToExternal(String line) {
        return line.replaceAll("'","''");
    }

    public void writeJavaPropertyFile(File outFile, JavaPropertyFile content) throws IOException {
        log.info("write:" + outFile);
        writeJavaPropertyFile(new FileWriter(outFile), content);
    }

    public void writeJavaPropertyFile(Writer outFile, JavaPropertyFile content) throws IOException {
        Writer writer = null;
        try {
            writer = new BufferedWriter(outFile);
            for (PropertyFileItem prop : content.getContent()) {
                log.finest("write:" + prop.getItem());
                writer.write(convertToExternal(prop.getPropertiesFileFormatted()));
                writer.write("\n");
            }
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                log.log(Level.SEVERE, "failed to close file:", ex);
            }
        }
    }


    /**
     * Get a list of files to work with
     * @param rootDir the root directory where to search (recursively)
     * @return list of files found
     */
    public FileList getFileList(File rootDir) {
        return new FileFinder().getFileList(rootDir);
    }

    public Writer createPOWriter(File outputFile) throws IOException {
        return new FileWriter(outputFile);
    }

    public Reader getPOFileReader(String lang) throws IOException {
        return new FileReader(getDefaultPOFileName(lang));
    }
}
