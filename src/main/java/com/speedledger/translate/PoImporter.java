package com.speedledger.translate;

import com.speedledger.base.logging.shared.LoggerFactory;
import org.fedorahosted.tennera.jgettext.Catalog;
import org.fedorahosted.tennera.jgettext.Message;
import org.fedorahosted.tennera.jgettext.PoParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * This class imports a .po-file into several java-property-files.
 */
public class PoImporter {
    private static Logger LOG = LoggerFactory.getLogger(PoImporter.class);
    private FileFinder finder = new FileFinder();
    private String defaultFileName = "out.po";

    /**
     * Import a .po-file
     * @param lang    language to use or empty for default
     * @throws IOException
     */
    public void Import(String lang) throws IOException {
        PoParser parser = new PoParser();
        Catalog cat = parser.parseCatalog(new File(defaultFileName));
        Iterator<Message> iterator = cat.iterator();
        ArrayList<Message> msgs = new ArrayList<Message>();
        String oldCtx = null;
        while (iterator.hasNext()) {
            Message msg = iterator.next();
            if (!msg.getMsgctxt().equals(oldCtx) && oldCtx != null) {
                handle(msgs, lang);
                msgs.clear();
            }
            msgs.add(msg);
            oldCtx = msg.getMsgctxt();
        }
        handle(msgs, lang); // handle unprocessed msgs
    }

    /**
     * Handle many messages from the same file (context)
     *
     * @param msgs message to process
     * @param lang language to use
     * @throws IOException
     */
    private void handle(ArrayList<Message> msgs, String lang) throws IOException {
        String packageName = msgs.get(0).getMsgctxt();
        FileItem item = finder.getFileList(new File(".")).getItemByPackage(packageName);
        if (item != null) {
            JavaPropertyFile property = JavaPropertyFileReader.readFile(item.getFile());
            boolean dirty = false;
            for (int row = 0; row < msgs.size(); row++) {
                Message msg = msgs.get(row);
                int idIndex = property.findItem(msg.getMsgid());
                if (idIndex >= 0) {
                    TranslatableItem val = (TranslatableItem) property.getContent().get(idIndex);
                    val.setValue(msg.getMsgstr());
                    dirty = true;
                } else {
                    LOG.warning("Warning: ignoring not found key:" + msg.getMsgid() + " in resource:" + packageName);
                }
            }
            if (dirty) {
                JavaPropertyFileWriter.write(item.getLangFile(lang), property);
            }
        } else {
            LOG.warning("Warning: ignoring not found resource:" + packageName);
        }
    }

    public static void main(String[] args) throws IOException {
        PoImporter importer = new PoImporter();
        String lang="";
        if(args.length>0) {
            lang=args[0];
        }
        importer.Import(lang);
    }
}
