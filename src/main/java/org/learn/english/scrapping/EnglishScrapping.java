package org.learn.english.scrapping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.learn.english.readers.AmericanHeritage;
import org.learn.english.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EnglishScrapping {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmericanHeritage.class);

    public void twoAndHalf(){
        String URL="https://www.springfieldspringfield.co.uk/view_episode_scripts.php?tv-show=two-and-a-half-men&episode=";//s01e01
         for(int i=10;i<=24;i++){
             try {
                 String fileName=  i<=9? "s01e0"+i:"s01e"+i;
                 Document document = Jsoup.connect(URL+fileName).get();
                 String script = document.getElementsByClass("scrolling-script-container").text();
                 FileUtil.writeData(script,FileUtil.DIR+"twoAndHalf\\"+fileName+".txt");
                 Thread.sleep(5000);
             } catch (IOException | InterruptedException e) {
                 LOGGER.error("twoAndHalf :: ");
             }

         }
    }


}
