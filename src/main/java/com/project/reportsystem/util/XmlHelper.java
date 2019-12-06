package com.project.reportsystem.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.reportsystem.domain.ReportStructure;
import com.project.reportsystem.exception.ReportFileException;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

@Log4j
@Component
public class XmlHelper extends AbstractFileHelper {

    public String createXmlFileByFileContent(String fileContent) {
        validate(fileContent);
        File jsonFile = getFile();
        byFileContent(fileContent, jsonFile);

        return jsonFile.getAbsolutePath();
    }

    public void updateXmlFileByFileContent(File file, String fileContent) {
        validate(fileContent);
        byFileContent(fileContent, file);
    }

    private void byFileContent(String fileContent, File gsonFile) {
        try (Writer writer = new FileWriter(gsonFile)) {
            JAXBContext jaxbContext = JAXBContext.newInstance(ReportStructure.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ReportStructure reportStructure = (ReportStructure) jaxbUnmarshaller.unmarshal(new StringReader(fileContent));
            Gson gson = new GsonBuilder().create();
            gson.toJson(reportStructure, writer);
        } catch (JAXBException | IOException e) {
            log.warn("Your file has wrong structure", e);
            throw new ReportFileException("Your file has wrong structure", e);
        }
    }

    private void validate(String content) {
        try {
            SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sFactory.newSchema(new File(PATH_TO_STORAGE + "schema.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(content)));
        } catch (SAXException | IOException e) {
            log.warn("Your file has wrong structure", e);
            throw new ReportFileException("Your file has wrong structure", e);
        }
    }
}
