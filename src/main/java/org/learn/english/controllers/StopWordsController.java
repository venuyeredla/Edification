package org.learn.english.controllers;


import org.learn.english.repositories.StopWordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/stopwords")
public class StopWordsController {
    @Autowired
    StopWordsRepository stopWordsRepository;

    @GetMapping
    public Set<String> getStopWords(){
        return  stopWordsRepository.getStopWords();
    }
}
