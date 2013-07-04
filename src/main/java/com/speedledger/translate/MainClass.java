package com.speedledger.translate;

import java.io.IOException;

/**
 * Main class for whole program.
 */
public class MainClass {
    /**
     * start translator, use arguments: import or export
     * @param args arguments to program
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String lang = "";
            if (args.length > 1) {
                lang = args[1];
            }
            if (args[0].equals("import")) {
                PoImporter importer = new PoImporter();
                importer.Import(lang);
            } else if (args[0].equals("export")) {
                PoExporter exporter = new PoExporter();
                exporter.export(lang);
            }
        } else {
            System.out.println("usage: import <lang>\n" +
                    "export <lang>");
        }
    }
}