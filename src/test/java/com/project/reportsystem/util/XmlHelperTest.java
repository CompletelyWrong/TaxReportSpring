package com.project.reportsystem.util;

import com.project.reportsystem.exception.ReportFileException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class XmlHelperTest {
    private static final String validFileContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<!-- Created with Liquid Technologies Online Tools 1.0 (https://www.liquid-technologies.com) -->\n" +
            "<root xsi:noNamespaceSchemaLocation=\"schema.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "  <fullName>Роик Максим Иванович</fullName>\n" +
            "  <type>Онлайн торговля</type>\n" +
            "  <innCode>1234567890</innCode>\n" +
            "  <periodStart>2001-09-21</periodStart>\n" +
            "  <periodEnd>1980-09-12</periodEnd>\n" +
            "  <incomeCode>2110</incomeCode>\n" +
            "  <incomeValue>88263</incomeValue>\n" +
            "  <outcomeCode>2129</outcomeCode>\n" +
            "  <outcomeValue>1150</outcomeValue>\n" +
            "  <percentCode>2100</percentCode>\n" +
            "  <percentValue>10801</percentValue>\n" +
            "  <clearCode>2200</clearCode>\n" +
            "  <clearValue>-</clearValue>\n" +
            "</root>";

    private final XmlHelper xmlHelper = new XmlHelper();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void createXmlFileByFileContentShouldThrowException() {
        expectedException.expect(ReportFileException.class);
        expectedException.expectMessage("Your file has wrong structure");
        xmlHelper.createXmlFileByFileContent("Invalid file content");
    }

    @Test
    public void createXmlFileByFileContentShouldCompleteWithOutExceptions() throws IOException {
        String filePath = xmlHelper.createXmlFileByFileContent(validFileContent);
        boolean actual = Files.deleteIfExists(new File(filePath).toPath());
        assertThat(actual, is(true));
    }

    @Test
    public void updateXmlFileByFileContentShouldThrowException() {
        expectedException.expect(ReportFileException.class);
        expectedException.expectMessage("Your file has wrong structure");
        xmlHelper.updateXmlFileByFileContent(new File("file path"), "Invalid file content");
    }

    @Test
    public void updateXmlFileByFileContentShouldCompleteWithOutExceptions() throws IOException {
        File file = new File("path");
        xmlHelper.updateXmlFileByFileContent(file, validFileContent);
        boolean actual = Files.deleteIfExists(file.toPath());
        assertThat(actual, is(true));
    }
}