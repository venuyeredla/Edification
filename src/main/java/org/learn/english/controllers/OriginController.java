package org.learn.english.controllers;


import org.learn.english.readers.EtymologyReader;
import org.learn.english.repositories.OriginReposiotry;
import org.learn.english.solr.DictionaryIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

@RestController
@RequestMapping("/origins")
public class OriginController {

    @Autowired
    OriginReposiotry originReposiotry;
    @Autowired
    EtymologyReader etymologyReader;
    @Autowired
    DictionaryIndexer dictionaryIndexer;

    @GetMapping("review")
    public Map<String, String> getOrigins(){
        Map<String, String> originsMap = originReposiotry.getOrigins();
        TreeMap<String ,String> treeMap=new TreeMap<>(originsMap);
        return treeMap;
    }

    @PostMapping("update")
    public void updateOrigin(@RequestBody Map<String,String> origin){
        originReposiotry.updateOrigin(origin.get("key"),origin.get("origin"));
    }

    @GetMapping("fetchorigin/{term}")
    public void updateOrigin(@PathVariable("term") String term){
        List<String> words = dictionaryIndexer.getWords(term);
        Map<String, String> originMap = etymologyReader.readOrigins(words);
        originMap.forEach((k,v)->{
            System.out.println(k+" -> "+v);
        });
    }


    @GetMapping(value = "delete")
    public String deletingExistingOrigins(){
        originReposiotry.deleteAll();;
        return "Delted sucessfully";
    }
}
