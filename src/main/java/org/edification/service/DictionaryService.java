package org.edification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.edification.crawler.EtymologyReader;
import org.edification.models.DictWord;
import org.edification.models.DictWordDef;
import org.edification.models.DictWordMeaning;
import org.edification.models.GWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DictionaryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryService.class);
    public static String GDICT ="https://api.dictionaryapi.dev/api/v1/entries/en/";

    @Autowired
    @Qualifier("dictionaryRepo")
    ObjectRepository<DictWord> dictionaryRepo;
    @Autowired
    EtymologyReader etymologyReader;
    @Autowired
    FileService fileService;

    @Autowired
    IndexerService indexerService;

    ObjectMapper objectMapper=new ObjectMapper();

    public Map<String,String> loadDictionaryFromFile() {
        Map<String,String> result=new HashMap<>();
        List<DictWord> dictWords = fileService.loadDictionaryFromFile();
        for (DictWord dictWord : dictWords) {
            dictWord.setId(dictWord.getWord());
            dictionaryRepo.insert(dictWord);
        }
        result.put("numberOfWords",String.valueOf(dictWords.size()));
        return result;
    }

     public String writeDictionaryToFile() {
         String filePath = fileService.writeDictionaryToFile(this.getAllDefinitions());
         return filePath;
    }

    private List<DictWord> getAllDefinitions(){
        List<DictWord> dictWords = dictionaryRepo.find().toList();
        return dictWords;
    }

    public boolean indexDictionary(boolean clean){
          this.indexerService.indexDictionary(this.getAllDefinitions());
        return true;
    }

    public List<DictWord> getDefintions(String quryTerm, boolean regex) {
        List<DictWord> words = new ArrayList<>();
        if (Objects.nonNull(quryTerm) && regex) {
            words = dictionaryRepo.find(ObjectFilters.regex("word", quryTerm)).toList();
        }else if(Objects.nonNull(quryTerm) && !regex){
            words = dictionaryRepo.find(ObjectFilters.eq("word", quryTerm)).toList();
        }
        LOGGER.info("No of results :: {}", words.size());
        return words;
    }

    public long getDictionarySize() {
        return dictionaryRepo.size();
    }

    public boolean readDefIndex(String word) {
        List<DictWord> dictWords = this.readDefintions(word);
        if(!CollectionUtils.isEmpty(dictWords)){
            dictWords.stream().filter(Objects::nonNull).forEach(def->{
                def.setId(def.getWord());
                def.setFull(true);
                this.dictionaryRepo.update(def);
                this.indexerService.indexDictionary(Arrays.asList(def));
            });
            return  true;
        }
        return false;
    }

    /**
     * Reads definitions from API.
     * @param word
     * @return
     */
    public List<DictWord> readDefintions(String word) {
         RestTemplate restTemplate = new RestTemplate();
            List<GWord> gwords = new ArrayList<>();
            try {
                ResponseEntity<GWord[]> forEntity = restTemplate.getForEntity(GDICT + word, GWord[].class);
                if (Objects.equals(forEntity.getStatusCode(), HttpStatus.OK)) {
                    LOGGER.info("Read Successfully  :: " + word);

                    List<GWord> words = Arrays.asList(forEntity.getBody());
                    LOGGER.info("Definition :: " + words.toString());
                    gwords.addAll(words);
                }
               return this.transform(gwords);
            } catch (Exception e) {
                LOGGER.error("Could not fetch definition for :: " + word);
            }
        return null;
    }

    private List<DictWord> transform(List<GWord> words){
        List<DictWord> dictWords=new ArrayList<>();
        words.stream().filter(Objects::nonNull).forEach(gWord->{
            DictWord dictWord =new DictWord();
            dictWord.setWord(gWord.getWord());
            if(Objects.nonNull(gWord.getOrigin())){
                dictWord.setOrigin(gWord.getOrigin().replace("?","'"));
            }
            List<DictWordMeaning> meanings=new ArrayList<>();
            dictWord.setMeanings(meanings);
            if(Objects.nonNull(gWord.getMeaning())){
                gWord.getMeaning().forEach((k,v)->{
                    DictWordMeaning dictWordMeaning =new DictWordMeaning();
                    dictWordMeaning.setPos(k);
                    meanings.add(dictWordMeaning);
                    //dictMeaning.setDefinitions();
                    //List<DictDef> dictDefs= (List<DictDef>) v;
                    List<HashMap> listMap= (List<HashMap>) v;
                    List<DictWordDef> converted = listMap.stream().map(def -> {
                        DictWordDef dictDef=  objectMapper.convertValue(def, DictWordDef.class);
                        return dictDef;
                    }).collect(Collectors.toList());
                    dictWordMeaning.setDefinitions(converted);
                });
                dictWords.add(dictWord);
            }else {
                System.out.println("No meaning for :: "+gWord.getWord());
            }
        });
        return dictWords;
    }

    public  Optional<String> readDefinition(String word){
        ////https://api.dictionaryapi.dev/api/v1/entries/en/egregious    -- new URL
        String url="https://googledictionaryapi.eu-gb.mybluemix.net/?lang=en&define=";
        RestTemplate restTemplate=new RestTemplate();
        Optional<String> optional=Optional.empty();
        try {
            ResponseEntity<String> forEntity = restTemplate.getForEntity(url+word, String.class);
            if(Objects.equals(forEntity.getStatusCode(), HttpStatus.OK)){
                optional= Optional.of(forEntity.getBody());
            }
        }catch(Exception e) {
            System.out.println("Exception occured for getting word meaning ::"+ word);
        }
        return optional;
    }

    @GetMapping("origin/read/{term}")
    public void updateOrigin(@PathVariable("term") String term){
        List<String> words = indexerService.getWords(term);
        Map<String, String> originMap = etymologyReader.readOrigins(words);
        originMap.forEach((k,v)->{
            System.out.println(k+" -> "+v);
        });
    }

}
