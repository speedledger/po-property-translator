package com.speedledger.translate;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Implementation of IO for mocking actual disk access during tests.
 */
class MockIO extends IO {

    private Map<File, String> fakeInputPropertyFiles = Maps.newHashMap();
    private StringWriter outputContents;
    private String inputPOFileContents = "";
    private Map<File, String> outputPropertyFiles = Maps.newHashMap();

    public void addFakePropertyFile(File file, String fileContents) {
        fakeInputPropertyFiles.put(file.getAbsoluteFile(), fileContents);
    }

    @Override
    public JavaPropertyFile readJavaPropertyFile(File file) throws IOException {
        String fileContents = fakeInputPropertyFiles.get(file.getAbsoluteFile());

        if (fileContents != null) {
            return readJavaPropertyFile(new StringReader(fileContents));
        }
        System.out.println("mock file not found: " + file);
        return null;
    }

    @Override
    public void writeJavaPropertyFile(File outFile, JavaPropertyFile content) throws IOException {
        System.out.println("write " + outFile);
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        super.writeJavaPropertyFile(charArrayWriter, content);
        outputPropertyFiles.put(outFile.getAbsoluteFile(), charArrayWriter.toString());
    }

    public String getWrittenPropertyFile(File file) {
        return outputPropertyFiles.get(file.getAbsoluteFile());
    }

    @Override
    public FileList getFileList(File rootDir) {
        return new FileList(FluentIterable.from(fakeInputPropertyFiles.keySet()).
                filter(new Predicate<File>() {
                    @Override
                    public boolean apply(File file) {
                        //list only reference language files, not translations like "test_sv.properties"
                        return !file.getName().contains("_");
                    }
                }).
                transform(new Function<File, PropertyFileMetadata>() {
                    public PropertyFileMetadata apply(File file) {
                        return new PropertyFileMetadata(file);
                    }
                }).toList());
    }

    @Override
    public Writer createPOWriter(File outputFile) throws IOException {
        outputContents = new StringWriter();
        return outputContents;
    }

    public String getPOOutputFileContents() {
        return outputContents.toString();
    }

    public void setMockInputPOFileContents(String contents) {
        this.inputPOFileContents = contents;
    }

    @Override
    public Reader getPOFileReader() {
        return new StringReader(inputPOFileContents);
    }


}
