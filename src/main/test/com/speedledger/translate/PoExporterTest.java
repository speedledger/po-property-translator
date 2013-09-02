package com.speedledger.translate;

import junit.framework.Assert;
import org.fedorahosted.tennera.jgettext.Message;
import org.fedorahosted.tennera.jgettext.PoParser;
import org.junit.Test;

import java.io.File;
import java.io.StringReader;
import java.util.Arrays;

/**
 *  Tests for the PoExporter util
 */
public class PoExporterTest {


    @Test
    public void exportSimpleCase() throws Exception {
        final MockIO mockIO = new MockIO();
        mockIO.addFakePropertyFile(new File("/resources/com/speedledger/test/test.properties"),
                "##To see these translations, go to File Menu -> Open File -> Click Help \n" +
                        "#This is a comment that should be ignored \n" +
                        "thisisatest=this is a test"
        );
        mockIO.addFakePropertyFile(new File("/resources/com/speedledger/test/test_sv.properties"),
                "#This is a comment that should be ignored \n" +
                        "thisisatest=detta är ett test"
        );

        PoExporter poExporter = new PoExporter() {
            @Override
            protected IO getIO() {
                return mockIO;
            }
        };

        //make the export to a PO file
        poExporter.export("sv");

        //verify the result
        String resultFileContents = mockIO.getPOOutputFileContents();

        //Use the real po parser and read the contents
        Message message = new PoParser().parseMessage(new StringReader(resultFileContents));

        //make sure the description comment from the reference language file is pressent AND
        //that the translation in the reference language is present as a comment
        Assert.assertEquals(Arrays.asList(
                "To see these translations, go to File Menu -> Open File -> Click Help",
                "this is a test"
        ), message.getComments());

        //make sure the id is correct
        Assert.assertEquals("thisisatest", message.getMsgid());

        //make sure the original translation is pressent
        Assert.assertEquals("detta är ett test", message.getMsgstr());

        //make sure the context is pressent
        Assert.assertEquals("com/speedledger/test/test", message.getMsgctxt());

        System.out.println("output: " + mockIO.getPOOutputFileContents());
    }

    @Test
    public void exportMultiline() throws Exception {
        final MockIO mockIO = new MockIO();
        mockIO.addFakePropertyFile(new File("/resources/com/speedledger/test/test.properties"),
                "multilineproperty=firstline \\" +
                        " secondline\\" +
                        " third line of the property"
        );
        mockIO.addFakePropertyFile(new File("/resources/com/speedledger/test/test_sv.properties"),
                "multilineproperty=firstline \\" +
                        " secondline\\" +
                        " third line of the property"
        );

        PoExporter poExporter = new PoExporter() {
            @Override
            protected IO getIO() {
                return mockIO;
            }
        };

        //make the export to a PO file
        poExporter.export("sv");

        //verify the result
        String resultFileContents = mockIO.getPOOutputFileContents();

        System.out.println(resultFileContents);
    }

}
