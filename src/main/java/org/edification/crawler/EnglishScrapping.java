package org.edification.crawler;

import org.edification.service.FileService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EnglishScrapping {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnglishScrapping.class);

      public void getScript(String seriesName, int seasonCount,int episodeCount ){
        String URL="https://www.springfieldspringfield.co.uk/view_episode_scripts.php?tv-show="+seriesName+"&episode=";
        FileService.createDirctories("scripts\\"+seriesName);
        for(int s=1;s<=seasonCount;s++){
            String season=s<=9?"s0"+s:"s"+s;
            for(int i=1;i<=episodeCount;i++){
                String episode = i<=9? "e0"+i:"e"+i;
                String fileName= season+episode;
                try {
                    Document document = Jsoup.connect(URL+fileName).get();
                    String script = document.getElementsByClass("scrolling-script-container").text();

                    FileService.writeDataToFile(script, FileService.getPath("scripts\\"+seriesName+"\\"+fileName+".txt"));
                    Thread.sleep(5000);
                } catch (IOException | InterruptedException e) {
                    LOGGER.error("twoAndHalf error for  :: "+fileName);
                }
            }

        }
    }


}
