package com.speedledger.translate;

import com.speedledger.base.logging.shared.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * class for writing java-property-files.
 */
public class JavaPropertyFileWriter {
    private static Logger LOG = LoggerFactory.getLogger(JavaPropertyFileWriter.class);

    /**
     * write java property file.
     * @param outFile file to write to
     * @param content content to write
     * @throws IOException
     */
    public static void write(File outFile, JavaPropertyFile content) throws IOException {
        LOG.info("write:" + outFile);
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
            for (JavaProperty prop : content.getContent()) {
                LOG.finest("write:"+prop.getItem());
                writer.write(prop.getPropertiesFileFormatted());
                writer.write("\n");
            }
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }

}
