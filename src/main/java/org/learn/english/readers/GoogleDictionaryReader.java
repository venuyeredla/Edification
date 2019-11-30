package org.learn.english.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.learn.english.models.GWord;
import org.learn.english.util.FileUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

@Service
public class GoogleDictionaryReader {
	
	String url="https://googledictionaryapi.eu-gb.mybluemix.net/?lang=en&define=";

    Gson gson=new Gson();
    Integer fileSuffix=new Integer(0);

    public void exportGoogleDict(){
       
        RestTemplate restTemplate=new RestTemplate();
        try {
            Optional<String> wordsString = FileUtil.readFileAsString(FileUtil.DIR + "input.json");
            wordsString.ifPresent(data->{
                String[] lists = gson.fromJson(data, String[].class);
                List<GWord> gwords=new ArrayList<>();

                Arrays.asList(lists).stream().forEach(word->{
                    try{
                        ResponseEntity<GWord[]> forEntity = restTemplate.getForEntity(url+word, GWord[].class);
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

    public void loadGdictionary() throws IOException{
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











