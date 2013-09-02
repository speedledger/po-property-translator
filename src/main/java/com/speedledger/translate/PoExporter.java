package com.speedledger.translate;

import com.speedledger.base.logging.shared.LoggerFactory;
import com.speedledger.translate.item.DescriptionCommentItem;
import com.speedledger.translate.item.PropertyFileItem;
import com.speedledger.translate.item.TranslatableItem;
import org.fedorahosted.tennera.jgettext.Catalog;
import org.fedorahosted.tennera.jgettext.Message;
import org.fedorahosted.tennera.jgettext.PoWriter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class exports all java-property-files as one .po-file.
 */
public class PoExporter {
    private static final Logger log = LoggerFactory.getLogger(PoExporter.class);


    /**
     * Export a .po-file
     * @param lang    language to use or empty for default
     * @throws IOException
     */
    public void export(String lang) throws IOException {
        log.info("export started lang:" + lang);
        List<PropertyFileMetadata> files = getIO().getFileList(new File(".")).getFiles();
        log.finest("items:" + files);
        Catalog export = new Catalog();
        for (PropertyFileMetadata file : files) {
            JavaPropertyFile data = getIO().readJavaPropertyFile(file.getLangFile(lang));
            JavaPropertyFile dataReferenceLang = getIO().readJavaPropertyFile(file.getFile());
            populateCatalog(file, data, dataReferenceLang, export);
        }
        PoWriter writer = new PoWriter();
        File outFile = new File(getIO().getDefaultPOFileName(lang));
        Writer fileWriter = getIO().createPOWriter(outFile);
        writer.write(export, fileWriter);
        log.info("export done, wrote:" + outFile.getAbsolutePath());
    }


    protected IO getIO() {
        return new IO();
    }


    private static void populateCatalog(PropertyFileMetadata file, JavaPropertyFile data, JavaPropertyFile dataReferenceLang, Catalog export) {
        String description = getDescriptionComment(dataReferenceLang);

        for (PropertyFileItem line : data.getContent()) {
            if (line instanceof TranslatableItem) {
                Message msg = new Message();
                TranslatableItem item = (TranslatableItem) line;
                msg.setMsgid(item.getKey());
                msg.setMsgstr(item.getValue());
                String packageName = file.getPackageAndNameWithoutExt();
                log.fine("packageName:" + packageName + ":");
                msg.setMsgctxt(packageName);
                if (!description.isEmpty()) {
                    msg.addComment(description);
                }
                PropertyFileItem referenceLang = dataReferenceLang.getItem(item.getKey());
                if (referenceLang != null) {
                    String referenceText = ((TranslatableItem) referenceLang).getValue();
                    if (!referenceText.isEmpty()) {
                        msg.addComment(referenceText);
                    }
                }

                export.addMessage(msg);
            }
        }
    }

    private static String getDescriptionComment(JavaPropertyFile dataReferenceLang) {
        String description = "";
        //find the description comment text from the reference language file
        for (PropertyFileItem line : dataReferenceLang.getContent()) {
            if (line instanceof DescriptionCommentItem) {
                description = ((DescriptionCommentItem) line).getItem();
                break;
            }
        }
        return description;
    }

    public static void main(String[] args) throws IOException {
        PoExporter exporter = new PoExporter();
        String lang = "";
        if (args.length > 0) {
            lang = args[0];
        }
        exporter.export(lang);
    }
}