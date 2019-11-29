package org.learn.english.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class FileUtil {

     public static String DIR="C:\\work\\wenglish\\";
    // public static String DIR="/local/mnt/projects/testbed/data/";

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


}
