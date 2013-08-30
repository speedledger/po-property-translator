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


    /**
     * Import a .po-file
     * @param lang    language to use or empty for default
     * @throws IOException
     */
    public void importPo(String lang) throws IOException {
        PoParser parser = new PoParser();
        Catalog cat = parser.parseCatalog(getIO().getPOFileReader(), false);
        Iterator<Message> iterator = cat.iterator();
        ArrayList<Message> msgs = new ArrayList<Message>();
        String oldCtx = null;
        FileList allFiles = getIO().getFileList(new File(".")); //finder.getFileList(new File("."));
        while (iterator.hasNext()) {
            Message msg = iterator.next();
            if (!msg.getMsgctxt().equals(oldCtx) && oldCtx != null) {
                handle(msgs, lang, allFiles);
                msgs.clear();
            }
            msgs.add(msg);
            oldCtx = msg.getMsgctxt();
        }
        handle(msgs, lang, allFiles); // handle unprocessed msgs
    }

    /**
     * Handle many messages from the same file (context)
     *
     * @param msgs message to process
     * @param lang language to use
     * @throws IOException
     */
    private void handle(ArrayList<Message> msgs, String lang, FileList allFiles) throws IOException {
        String packageName = msgs.get(0).getMsgctxt();
        PropertyFileMetadata item = allFiles.getItemByPackage(packageName);
        if (item != null) {
            JavaPropertyFile property = getIO().readJavaPropertyFile(item.getLangFile(lang));
            boolean dirty = false;
            for (int row = 0; row < msgs.size(); row++) {
                Message msg = msgs.get(row);
                TranslatableItem val = property.getItem(msg.getMsgid());
                if (val != null) {
                    val.setValue(msg.getMsgstr());
                    dirty = true;
                } else {
                    LOG.warning("Warning: ignoring not found key:" + msg.getMsgid() + " in resource:" + packageName);
                }
            }
            if (dirty) {
                getIO().writeJavaPropertyFile(item.getLangFile(lang), property);
            }
        } else {
            LOG.warning("Warning: ignoring not found resource:" + packageName);
        }
    }


    protected IO getIO() {
        return new IO();
    }
}
