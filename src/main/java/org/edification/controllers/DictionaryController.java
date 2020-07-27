package org.edification.controllers;

import org.edification.models.DictWord;
import org.edification.service.ClausesService;
import org.edification.service.DictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dict")
public class DictionaryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryController.class);

    @Autowired
    DictionaryService dictionaryService;
    
    @GetMapping("define/{word}")
    public List<DictWord> getDefintions(@PathVariable(name = "word",required = true) String query,@RequestParam(name = "regex",defaultValue = "false") boolean regex){
        LOGGER.info("Query is = {}",query);
        return this.dictionaryService.getDefintions(query,regex);
    }

    @GetMapping("readDef/{word}")
    public String getDefintion(@PathVariable(name = "word",required = true) String word){
        LOGGER.info("Reading the word = {}",word);
        this.dictionaryService.readDefIndex(word);
        return "SUCESS";
    }

    @GetMapping("dictsize")
    public Map<String,Long> getDictSize(){
         Map<String,Long> countMap=new HashMap<>();
         countMap.put("dictionary",dictionaryService.getDictionarySize());
        return countMap;
    }

    @GetMapping("import")
    public Map<String, String> importToDB(){
        Map<String, String> result = this.dictionaryService.loadDictionaryFromFile();
        return result;
     }

    @GetMapping("export")
    public void exportToFile(){
        this.dictionaryService.writeDictionaryToFile();
    }

    @GetMapping("index")
    public boolean indexDictionary(@RequestParam(name = "clean",required = false) boolean clean){
        dictionaryService.indexDictionary(clean);
        return true;
    }

}
