package com.example.pravnaInformatika.service;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Service
public class LawService {

	private Document legalDocument;

    public LawService() {
 
    }

    public String loadLaw412Document(String relativePath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(relativePath);
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found " + relativePath);
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlDocument = dBuilder.parse(inputStream);
            inputStream.close();
            StringBuilder lawText = new StringBuilder();

            String chapterNumber = xmlDocument.getElementsByTagName("num").item(0).getTextContent().trim();
            String heading = xmlDocument.getElementsByTagName("heading").item(0).getTextContent().trim();
            lawText.append(heading).append("\n\nČlan ").append(chapterNumber).append("\n\n");

            NodeList points = xmlDocument.getElementsByTagName("point");
            for (int i = 0; i < points.getLength(); i++) {
                Element point = (Element) points.item(i);
                String num = point.getElementsByTagName("num").item(0).getTextContent().trim();
                String content = point.getElementsByTagName("content").item(0).getTextContent().trim();
                lawText.append(num).append(" ").append(content).append("\n\n");
            }

            return lawText.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String loadLaw413Document(String relativePath) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(relativePath);
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found " + relativePath);
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document xmlDocument = dBuilder.parse(inputStream);
            inputStream.close();
            StringBuilder lawText = new StringBuilder();
            
            String articleNumber = xmlDocument.getElementsByTagName("num").item(0).getTextContent().trim();
            String title = xmlDocument.getElementsByTagName("heading").item(0).getTextContent().trim();
            lawText.append(title).append("\n\nČlan ").append(articleNumber).append("\n\n");

            NodeList contents = xmlDocument.getElementsByTagName("content");
            if (contents.getLength() > 0 && contents.item(0).getParentNode().getNodeName().equals("paragraph")) {
                String content = contents.item(0).getTextContent().trim();
                lawText.append(content).append("\n\n");
            }

            NodeList points = xmlDocument.getElementsByTagName("point");
            for (int i = 0; i < points.getLength(); i++) {
                Element point = (Element) points.item(i);
                String num = point.getElementsByTagName("num").item(0).getTextContent().trim();
                String pointContent = point.getElementsByTagName("content").item(0).getTextContent().trim();
                lawText.append(num).append(" ").append(pointContent).append("\n\n");
            }

            return lawText.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
