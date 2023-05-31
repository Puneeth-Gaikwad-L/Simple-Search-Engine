package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

public class Crawler {
    HashSet<String> UrlSet;

    int MaxDepth = 2;
    Crawler(){
        UrlSet = new HashSet<String>();
    }

    public void getPageTextsAndLinks(String Url , int depth){
        if(UrlSet.contains(Url)){
            return;
        }
        if(depth>=MaxDepth){
            return;
        }
        if(UrlSet.add(Url)){
            System.out.println(Url);
        }
        depth++;
        try {
            Document document = Jsoup.connect(Url).timeout(5000).get();
//        Indexer work starts here
            Indexer indexer = new Indexer(document, Url);
            System.out.println(document.title());
            Elements availablelinkpages = document.select("a[href]");
            for (Element currentLink : availablelinkpages) {
                getPageTextsAndLinks(currentLink.attr("abs:href"), depth);
            }
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Crawler c = new Crawler();
        c.getPageTextsAndLinks("https://www.javatpoint.com",0);
//        c.getPageTextsAndLinks("https://www.geeksforgeeks.org",0);
    }
}