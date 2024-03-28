package com.example.pravnaInformatika.controller;

import com.example.pravnaInformatika.service.DocumentService;
import com.example.pravnaInformatika.service.LawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/doc")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/load")
    public String loadDocument(@RequestParam String path) {
        return "";
    }

    @GetMapping("/generate-metadata")
    public void generateMetadata() {
        documentService.run();
        System.out.println("Successfully generated metadata.txt!");
    }
}
