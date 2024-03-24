package com.example.pravnaInformatika.service;
import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class LawService {

	private Document legalDocument;

    public LawService() {
 
    }

    public String loadLawDocument(String relativePath) {
        StringBuilder plainText = new StringBuilder();
        try {
            System.out.println(relativePath);
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(relativePath);
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found " + relativePath);
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);

            // Assuming "point" is your node of interest
            NodeList points = doc.getElementsByTagName("point");
            for (int i = 0; i < points.getLength(); i++) {
                Node point = points.item(i);
                NodeList children = point.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if ("content".equals(child.getNodeName())) {
                        // Assuming <p> contains the text
                        NodeList paragraphs = child.getChildNodes();
                        for (int k = 0; k < paragraphs.getLength(); k++) {
                            Node paragraph = paragraphs.item(k);
                            if (paragraph.getNodeName().equals("p")) {
                                plainText.append(paragraph.getTextContent().trim());
                                plainText.append("\n\n");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error converting XML to plain text";
        }
        return plainText.toString();
    }

    public Document getLegalDocument() {
        return legalDocument;
    }
}
