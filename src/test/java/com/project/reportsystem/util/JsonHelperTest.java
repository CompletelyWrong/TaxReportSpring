package com.project.reportsystem.util;

import com.project.reportsystem.domain.ReportStructure;
import com.project.reportsystem.exception.ReportFileException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class JsonHelperTest {
    private static final String validFileContent = "{\"fullName\":\"Роик Максим Иванович\",\n" +
            "\"type\":\"Онлайн торговля\",\n" +
            "\"innCode\":\"1234567890\",\n" +
            "\"periodStart\":\"2019-11-01\",\n" +
            "\"periodEnd\":\"2019-11-02\",\n" +
            "\"incomeCode\":\"2110\",\n" +
            "\"incomeValue\":\"88263\",\n" +
            "\"outcomeCode\":\"2129\",\n" +
            "\"outcomeValue\":\"1150\",\n" +
            "\"percentCode\":\"2100\",\n" +
            "\"percentValue\":\"10801\",\n" +
            "\"clearCode\":\"2200\",\n" +
            "\"clearValue\":\"-\"}";

    private final JsonHelper jsonHelper = new JsonHelper();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void readFromJsonShouldThrowExceptionReportFileException() {
        expectedException.expect(ReportFileException.class);
        expectedException.expectMessage("Your file was corrupted");
        jsonHelper.readFromJson("Invalid file Url");
    }

    @Test
    public void createJsonFileByFileContentShouldThrowExceptionReportFileException() {
        expectedException.expect(ReportFileException.class);
        expectedException.expectMessage("Your file has wrong structure");
        jsonHelper.createJsonFileByFileContent("Invalid file content");
    }

    @Test
    public void createJsonFileByFileContentShouldCompleteWithOutExceptions() throws IOException {
        String filePath = jsonHelper.createJsonFileByFileContent(validFileContent);
        boolean actual = Files.deleteIfExists(new File(filePath).toPath());
        assertThat(actual, is(true));
    }

    @Test
    public void updateJsonFileByFileContentShouldThrowExceptionReportFileException() {
        expectedException.expect(ReportFileException.class);
        expectedException.expectMessage("Your file has wrong structure");
        jsonHelper.updateJsonFileByForm(null, null);
    }


    @Test
    public void createJsonFileByFormShouldThrowExceptionReportFileException() {
        expectedException.expect(ReportFileException.class);
        expectedException.expectMessage("Your file has wrong structure");
        jsonHelper.createJsonFileByForm(null);

    }

    @Test
    public void updateJsonFileByFormShouldCompleteWithOutExceptions() throws IOException {
        String filePath = jsonHelper.createJsonFileByForm(new ReportStructure());
        boolean actual = Files.deleteIfExists(new File(filePath).toPath());
        assertThat(actual, is(true));
    }
}