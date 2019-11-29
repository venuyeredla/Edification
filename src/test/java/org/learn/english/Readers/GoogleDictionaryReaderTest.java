package org.learn.english.Readers;

import com.google.gson.Gson;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.learn.english.config.EnglishTestConfig;
import org.learn.english.models.GWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {EnglishTestConfig.class})
public class GoogleDictionaryReaderTest {

    @Autowired
    private GoogleDictionaryReader googleDictionaryReader;

    @Test
    @Ignore
    public void testGooglReader(){
        googleDictionaryReader.exportGoogleDict();
    }
    @Test
    public void printGDict(){

        googleDictionaryReader.loadGdictionary();
    }


    @Test
    @Ignore
    public void conversionTest() throws IOException, URISyntaxException {
        Gson gson=new Gson();
        Path path = Paths.get(ClassLoader.getSystemResource("googledict.json").toURI());
        String str =new String(Files.readAllBytes(path));
        str= str.substring(1, str.length()-1);
        GWord gWord = gson.fromJson(str,  GWord.class);
     //   JsonElement jsonTree = gson.toJsonTree(str);

        gWord.getMeaning().forEach((k,v) ->{



        });

        System.out.println("venu");


    }

}