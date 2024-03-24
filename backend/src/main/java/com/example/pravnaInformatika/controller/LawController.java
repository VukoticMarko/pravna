package com.example.pravnaInformatika.controller;

import com.example.pravnaInformatika.service.LawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

@RestController
@RequestMapping("/law")
public class LawController {

    private final LawService lawService;

    @Autowired
    public LawController(LawService lawService) {
        this.lawService = lawService;
    }

    @GetMapping("/load")
    public String loadLaw(@RequestParam String path) {

        return lawService.loadLawDocument(path);
    }
}
