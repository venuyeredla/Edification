package org.edification.crawler;


import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class EnglishCrawler extends WebCrawler {
    private final static Pattern EXCLUSIONS
            = Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL().toLowerCase();
        return !EXCLUSIONS.matcher(urlString).matches()
                && urlString.startsWith("https://www.springfieldspringfield.co.uk/");
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL ="+url);
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            String script = document.getElementsByClass("scrolling-script-container").text();
            System.out.println(script);
            //FileUtil.writeData(script,FileUtil.DIR+"twoAndHalf.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void visitOld(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL ="+url);
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String title = htmlParseData.getTitle();
             //     System.out.println(title);
            String text = htmlParseData.getText();
           // System.out.println(text);
           // String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
           // do something with the collected data
        }
    }
}
