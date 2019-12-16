package org.learn.english.controllers;


import org.learn.english.models.Word;
import org.learn.english.repositories.EnglishRepository;
import org.learn.english.repositories.OriginReposiotry;
import org.learn.english.solr.DictionaryIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dictionary")
public class DicitonaryController {


    @Autowired
    DictionaryIndexer dictionaryIndexer;

    @Autowired
    EnglishRepository englishRepository;
    @Autowired
    OriginReposiotry originReposiotry;

    @GetMapping("updateOrigins")
    public void updateOrigins(){
        Map<String, String> origins = originReposiotry.getOrigins();
        List<Word> tobeIndexed = englishRepository.updateOrigins(origins);
        dictionaryIndexer.indexDictionary(tobeIndexed);
    }

    @GetMapping("export")
    public void exportDictionary(){
        englishRepository.writeToFile();;
    }
}
