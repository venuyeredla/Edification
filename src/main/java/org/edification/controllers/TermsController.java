package org.edification.controllers;


import org.edification.nlp.NlpAnalysisService;
import org.edification.service.TermsService;
import org.edification.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/review")
public class TermsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TermsController.class);

    @Autowired
    TermsService termsService;

    @Autowired
    NlpAnalysisService nlpAnalysis;

    @GetMapping("known")
    public Set<String> getKnownWords(){
        return  termsService.getStopWords();
    }

    @GetMapping("listnewwords")
    public List<String> listNewWords(){
        Map<String, Long> unknownWordsWithFreq = nlpAnalysis.getUnknownWords(FileService.ROOT_DIR+"reader\\");
        List<String> newWordsList=new ArrayList<>();
        unknownWordsWithFreq.forEach((k,v)->{
            newWordsList.add(k +"--"+v);
        });
        return newWordsList;
    }

    @RequestMapping("addnew/{word}")
    public boolean addNewWord(@PathVariable("word") String word){
        boolean isAdded = this.termsService.addNewWord(word);
        LOGGER.info("Added new known word :: "+word);
        return isAdded;
    }

    @RequestMapping("isexisit/{word}")
    public boolean isExisted(@PathVariable("word") String word){
        boolean isAdded = this.termsService.doesExisits(word);
        LOGGER.info("Added new known word :: "+word);

        return isAdded;
    }

    @GetMapping("import")
    public boolean loadStopWords(){
        return  termsService.loadStopWords();
    }

    @GetMapping("export")
    public void writeDictionaryTest(){
        this.termsService.writeKnownWordsTofile();
    }


}
