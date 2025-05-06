package org.example;

import java.util.Set;
import java.util.concurrent.Phaser;

public class CrawlerTask implements Runnable {

    private final URLStore urlStore;
    private final URLFetcher urlFetcher;
    private final int maxDepth;
    private final int currentDepth;
    private final Phaser phaser;
    //phaser supports registration and de-registration of threads.

    public CrawlerTask(URLStore urlStore, URLFetcher urlFetcher, int maxDepth, int currentDepth, Phaser phaser) {
        this.urlStore = urlStore;
        this.urlFetcher = urlFetcher;
        this.maxDepth = maxDepth;
        this.currentDepth = currentDepth;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        try{
            String url=urlStore.getNextURL();
            System.out.println(Thread.currentThread().getName()+" "+url);
            if(url==null||currentDepth>maxDepth) return;
            //Thread.currentThread() — gets the thread currently running this code.
            //.getName() — returns the thread's name (e.g., "Thread-1", "pool-1-thread-3").
            //Thread-2 https://example.com/about

            Set<String>links=urlFetcher.fetchLinks(url);
            for(String link:links){
                if(urlStore.addUrl(link)){
                    phaser.register();
                    WebCrawler.submitTask(urlStore,urlFetcher,currentDepth+1,maxDepth);
                }
            }
        } catch (Exception e) {
            System.out.println("Error Occurred");
        }
        finally {
            phaser.arriveAndDeregister();
        }
    }
}
