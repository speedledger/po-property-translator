package com.speedledger.translate;

import com.speedledger.base.logging.shared.LoggerFactory;
import org.fedorahosted.tennera.jgettext.Catalog;
import org.fedorahosted.tennera.jgettext.Message;
import org.fedorahosted.tennera.jgettext.PoWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class exports all java-property-files as one .po-file.
 */
public class PoExporter {
    private static Logger LOG = LoggerFactory.getLogger(PoExporter.class);
    private FileFinder finder = new FileFinder();
    private String defaultFileName = "out.po";

    /**
     * Export a .po-file
     * @param lang    language to use or empty for default
     * @throws IOException
     */
    public void export(String lang) throws IOException {
        LOG.info("export started lang:"+lang);
        List<FileItem> files = finder.getFileList(new File(".")).getFiles();
        LOG.finest("items:" + files);
        Catalog export = new Catalog();
        for (FileItem file : files) {
            JavaPropertyFile data = JavaPropertyFileReader.readFile(file.getLangFile(lang));
            populateCatalog(file, data, export);
        }
        PoWriter writer = new PoWriter();
        File outFile = new File(defaultFileName);
        writer.write(export, outFile);
        LOG.info("export done, wrote:" + outFile.getAbsolutePath());
    }

    private static void populateCatalog(FileItem file, JavaPropertyFile data, Catalog export) {
        for (JavaProperty line : data.getContent()) {
            Message msg = new Message();
            if (line instanceof TranslatableItem) {
                TranslatableItem item = (TranslatableItem) line;
                msg.setMsgid(item.getKey());
                msg.setMsgstr(item.getValue());
                String packageName = file.getPackageAndNameWithoutExt();
                LOG.fine("packageName:"+packageName+":");
                //msg.addSourceReference(packName);
                msg.setMsgctxt(packageName);
                export.addMessage(msg);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        PoExporter exporter = new PoExporter();
        String lang="";
        if(args.length>0) {
            lang=args[0];
        }
        exporter.export(lang);
    }
}