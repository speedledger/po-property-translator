package com.speedledger.translate;


import junit.framework.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Tests for the PoImporter util
 */
public class PoImporterTest {

    @Test
    public void importSimple() throws Exception {
        final MockIO mockIO = new MockIO();

        mockIO.addFakePropertyFile(new File("/resources/com/speedledger/test/test.properties"),
                "##To see these translations, go to File Menu -> Open File -> Click Help \n" +
                        "#This is a comment that should be ignored \n" +
                        "thisisatest=this is a test"
        );
        mockIO.addFakePropertyFile(new File("/resources/com/speedledger/test/test_sv.properties"),
                "#This is a comment that should be ignored but present in the output\n" +
                        "thisisatest=this is not a correct translation and should be replaced in the ouput"
        );

        mockIO.setMockInputPOFileContents(
                "# To see these translations, go to File Menu -> Open File -> Click Help\n" +
                        "# this is a test\n" +
                        "msgctxt \"com/speedledger/test/test\"\n" +
                        "msgid \"thisisatest\"\n" +
                        "msgstr \"detta är ett test\""
        );

        PoImporter importer = new PoImporter() {
            @Override
            protected IO getIO() {
                return mockIO;
            }
        };

        //do the import
        importer.importPo("sv");

        //verify the result
        String result = mockIO.getWrittenPropertyFile(new File("/resources/com/speedledger/test/test_sv.properties"));

        //make sure the comment only present in the original sv file is still there
        Assert.assertTrue(result.contains("#This is a comment that should be ignored but present in the output"));

        //make sure the old translation is replaced with the new one from the PO file.
        Assert.assertTrue(result.contains("thisisatest=detta är ett test"));

        System.out.println(result);
    }

}
