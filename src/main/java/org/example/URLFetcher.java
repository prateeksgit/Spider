package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class URLFetcher {
    public Set<String> fetchLinks(String url){
        Set<String> links=new HashSet<>();
        //taking HashSet for unique values. duplicates will not get added.
        Document document=null;
        //jsoup document created
        try{
            document= Jsoup.connect(url).timeout(50000).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //jsoup connects with the given URL scans the whole web and stores it as document.

        System.out.println(document.text());
        Elements anchorTags=document.select("a[href]");
        //Elements is kind of array of element of HTML tag
        //A JSoup collection that holds multiple Element objects.
        //uses JSoup to extract all <a> (anchor) tags from an HTML document that contain an href attribute — meaning they are actual links.
        for(Element link:anchorTags){
            String extractedUrl=link.absUrl("href");
            if(!extractedUrl.isEmpty()){
                links.add(extractedUrl);
                System.out.println(links);
            }
        }
        return links;
    }
}
