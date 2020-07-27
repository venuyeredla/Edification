package org.edification.service;

import com.google.gson.Gson;
import org.edification.models.Clause;
import org.edification.models.DictWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 *     Paths should be relative to FleUtil.DIR=
 */

@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    public static String ROOT_DIR="C:\\work\\opensource\\engdata\\";
    public static final String CLAUSES_JSON = "clauses.json";
    public static final String DICTIONARY_JSON = "dictionary.json";
    public static final String STOPWORDS_JSON = "stopwords.json";

    @Autowired
    Gson gson=new Gson();

    public static String getDBPath(String dbName){
         return ROOT_DIR+"db\\"+dbName;
     }
    public static Path getPath(String fileName){
        return Paths.get(ROOT_DIR+"files\\"+fileName) ;
    }

    public List<DictWord> loadDictionaryFromFile() {
        List<DictWord> dictWords=new ArrayList<>();
        Optional<String> dataOptional = FileService.readFileAsString(getPath(DICTIONARY_JSON));
        if(dataOptional.isPresent()){
            dictWords = Arrays.asList(gson.fromJson(dataOptional.get(), DictWord[].class));
            for (DictWord dictWord : dictWords) {
                dictWord.setId(dictWord.getWord());
            }
         }
        LOGGER.info("No of words in dictionary : {}", dictWords.size());
        return dictWords;
    }

    public String writeDictionaryToFile(List<DictWord> dictWords) {
        String filePath = writeDataToFile(gson.toJson(dictWords), getPath(DICTIONARY_JSON));
        LOGGER.info("No of words in dictionary :: " + dictWords.size());
        LOGGER.info("File Path :: " + dictWords.size());
        return filePath;
    }

    public Set<String> loadStopWords(){
        Optional<String> stopWordsOptional = FileService.readFileAsString(getPath(STOPWORDS_JSON));
        TreeSet<String> sortedSet=new TreeSet<>();
        if(stopWordsOptional.isPresent()){
            String[] words = gson.fromJson(stopWordsOptional.get(), String[].class);
            Arrays.asList(words).stream().forEach(stopword->{
                sortedSet.add(stopword);
            });
            System.out.println("Stop words size :: "+sortedSet.size());
            return sortedSet;
        }
        return  sortedSet;
    }

    public String writeKnownWordsTofile(Set<String> stopWords) {
        LOGGER.info("No of words in known words :: " + stopWords.size());
        String filePath = writeDataToFile(gson.toJson(stopWords), FileService.getPath(STOPWORDS_JSON) );
        return filePath;
    }

    public List<Clause> readClausesFromFile(){
        List<Clause> clauses=new ArrayList<>();
        Optional<String> clausesOptional=  FileService.readFileAsString(FileService.getPath(CLAUSES_JSON));
        if(clausesOptional.isPresent()){
            Clause[] clausesArray = gson.fromJson(clausesOptional.get(), Clause[].class);
            clauses = Arrays.asList(clausesArray).stream().filter(Objects::nonNull).collect(Collectors.toList());
            LOGGER.info("No of clauses ::"+clauses.size() );
        }
        return clauses;
    }

    public String writeClausesTofile(List<Clause> clauses) {
        String filePath = writeDataToFile(gson.toJson(clauses), FileService.getPath(CLAUSES_JSON) );
        LOGGER.info("No of clauses written to file :: " + clauses.size());
        return filePath;
    }

    public static void createDirctories(String relativePath){
        Path path = Paths.get(ROOT_DIR+relativePath);
        //if directory exists?
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Optional<String> readFileAsString(Path filePath) {
        Optional<String> data = Optional.empty();
        try {
            byte[] bytes = Files.readAllBytes(filePath);
            data = Optional.of(new String(bytes));
        } catch (IOException e) {
            System.out.println("Can't load file :" + filePath + "  --  " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    public static String writeDataToFile(String data, Path filePath) {
        try {
            Files.write(filePath, data.getBytes());
            LOGGER.info("Written data to :: " + filePath.toFile().getAbsolutePath());
            return filePath.toFile().getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads file from classpath.
     * @param resourceName
     * @return
     */
    public static Optional<String> readResourceAsString(String resourceName) {
        Optional<String> data = Optional.empty();
        try {
            URI uri = ClassLoader.getSystemResource(resourceName).toURI();
            byte[] bytes = Files.readAllBytes(Paths.get(uri));
            data = Optional.of(new String(bytes));
        } catch (IOException | URISyntaxException e) {
            System.out.println("Can't load file :" + resourceName + "  --  " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }
}
