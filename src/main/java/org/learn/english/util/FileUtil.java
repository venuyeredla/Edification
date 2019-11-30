package org.learn.english.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;

public class FileUtil {

//     public static String DIR="C:\\work\\wenglish\\";
     public static String DIR="/usr/local/Code/data/Latest/";

    public static Optional<String> readResourceAsString(String resourceName){
        Optional<String> data=Optional.empty();
        try {
            URI uri = ClassLoader.getSystemResource(resourceName).toURI();
            byte[] bytes = Files.readAllBytes(Paths.get(uri));
            data = Optional.of(new String(bytes));
        } catch (IOException | URISyntaxException e) {
            System.out.println("Can't load file :"+resourceName +"  --  "+e.getMessage());
            e.printStackTrace();
        }
        return  data;
    }

    public static Optional<String> readFileAsString(String fileName){
        Optional<String> data=Optional.empty();
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(fileName));
            data = Optional.of(new String(bytes));
        } catch (IOException e) {
            System.out.println("Can't load file :"+fileName +"  --  "+e.getMessage());
            e.printStackTrace();
        }
        return  data;
    }
    
    public static void writeData(String data, String filePath){
    	try {
			Files.write(Paths.get(filePath), data.getBytes());
			System.out.println("Written data to :: "+filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    	
    public static void sortWords() {
    	String filePath="";
    	Optional<String> readFileAsString = readFileAsString(DIR+"nonRoots.json");
    	readFileAsString.ifPresent(data->{
    		Gson gson=new Gson();
    		String[] words = gson.fromJson(data, String[].class);
    		List<String> asList = Arrays.asList(words);
    		Collections.sort(asList);
    		writeData(gson.toJson(asList), DIR+"nonRoots.json");
    		
    	});
    }
    


}
