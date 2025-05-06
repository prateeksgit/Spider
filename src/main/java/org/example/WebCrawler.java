package org.example;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class WebCrawler {
    private static Phaser phaser;
    private static ExecutorService executorService;
    //when we create new thread executor service will handle the number of threads to be used

    public static void main(String[]args){
        Scanner sc= new Scanner(System.in);
        System.out.println("Enter your URL");
        String url=sc.nextLine();

        System.out.println("Enter the depth of crawler:");
       final int MAX_Depth=sc.nextInt();

        System.out.println("Enter the number of workers(thread):");
        final int max_thread=sc.nextInt();

        URLStore urlStore=new URLStore();
        URLFetcher urlFetcher=new URLFetcher();
        phaser=new Phaser(1);
        executorService= Executors.newFixedThreadPool(max_thread);
        urlStore.addUrl(url);
        long start=System.currentTimeMillis();
        submitTask(urlStore,urlFetcher,0,MAX_Depth);
        phaser.awaitAdvance(phaser.getPhase());
        executorService.shutdown();
        long end=System.currentTimeMillis();
        System.out.print("Time taken: ");
        System.out.print(end-start);

    }

    public static void submitTask(URLStore urlStore, URLFetcher urlFetcher, int currentDepth, int maxDepth) {
        executorService.submit(new CrawlerTask(urlStore,urlFetcher,maxDepth,currentDepth,phaser));
    }
}
