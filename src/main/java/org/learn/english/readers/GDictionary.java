package org.learn.english.readers;

import com.google.gson.Gson;
import org.learn.english.models.GWord;
import org.learn.english.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Component
public class GDictionary extends DictionaryReaderBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GDictionary.class);

    RestTemplate restTemplate=new RestTemplate();
    @Autowired
    Gson gson;
    Integer fileSuffix=new Integer(0);
    static String URL="https://googledictionaryapi.eu-gb.mybluemix.net/?lang=en&define=";

    @Override
    public String getOrigin(String word) {
        try {
            String origin=null;
            ResponseEntity<GWord[]> forEntity = restTemplate.getForEntity(URL+word, GWord[].class);
            if(Objects.equals(forEntity.getStatusCode(), HttpStatus.OK)){
                List<GWord> words = Arrays.asList(forEntity.getBody());
                if(Objects.nonNull(words) && words.size()>0){
                  origin= words.get(0).getOrigin();
                }
                LOGGER.info("GDictOriginReader :: {} -> {} ",word,origin);
            }
            return origin;
        } catch (Exception e) {
            LOGGER.error("GDictOriginReader :: {}",word);
        }
        return null;
    }



    public void exportGoogleDict(){

        RestTemplate restTemplate=new RestTemplate();
        try {
            Optional<String> wordsString = FileUtil.readFileAsString(FileUtil.DIR + "input.json");
            wordsString.ifPresent(data->{
                String[] lists = gson.fromJson(data, String[].class);
                List<GWord> gwords=new ArrayList<>();

                Arrays.asList(lists).stream().forEach(word->{
                    try{
                        ResponseEntity<GWord[]> forEntity = restTemplate.getForEntity(URL+word, GWord[].class);
                        if(Objects.equals(forEntity.getStatusCode(), HttpStatus.OK)){
                            System.out.println("Read Successfully  :: "+word);
                            List<GWord> words = Arrays.asList(forEntity.getBody());
                            gwords.addAll(words);
                        }
                        if(gwords.size()>5000){
                            FileUtil.writeData(gson.toJson(gwords), FileUtil.DIR + "definitions/defs"+fileSuffix.toString()+".json");
                            gwords.clear();
                            fileSuffix=new Integer(fileSuffix.intValue()+1);
                        }
                    }catch (Exception e){
                        System.out.println("Could not fetch definition for :: "+word);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                System.out.println("writing last list");
                FileUtil.writeData(gson.toJson(gwords), FileUtil.DIR + "google\\defs"+fileSuffix.toString()+".json");
                fileSuffix=new Integer(fileSuffix.intValue()+1);

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  Optional<String> readDefinition(String word){
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

    public void loadGdictionary() throws IOException {
        Optional<String> googleData = FileUtil.readFileAsString(FileUtil.DIR+"GoogleDict1.json");
        if(googleData.isPresent()){
            Map<String,String> originMap=new HashMap<>();
            GWord[] words = gson.fromJson(googleData.get(), GWord[].class);
            long count= Arrays.asList(words).stream().filter(gWord -> Objects.nonNull(gWord.getOrigin())).count();

            Arrays.asList(words).stream().filter(gWord -> Objects.nonNull(gWord.getOrigin())).forEach(gWord ->{
                System.out.println(gWord.getWord()+"------"+gWord.getOrigin().replace("?","'"));
                //originMap.put(StringUtils.lowerCase(gWord.getWord()), gWord.getOrigin().replace("?","'"));
            });
            System.out.println("size "+count);

            // Files.writeFile(gson.toJson(originMap),new File(FileUtil.DIR + "origins.json"));
        }

    }
}
