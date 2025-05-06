package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class URLStore {
    private final ConcurrentHashMap<String, Boolean> visitedURL = new ConcurrentHashMap<>();
    //Using concurrentHashMap to ensure safe concurrent read/Writes
    //a thread safe way to keep track of which URLs have already been visited.

    private final BlockingQueue<String> urlQueue = new LinkedBlockingQueue<>();
    //tracks which URL have been visited
    //prevents reprocessing same URL
    // linked blockingqueue beacuse mutiple threads can safely put or add url and take crawl.
    //grows as needed casue we don't know number of links unlike arrayBlockingueue
    //also has blocking behaviour once queue is empty.


    public boolean addUrl(String url) {
        if (visitedURL.putIfAbsent(url, true) == null) {
            urlQueue.add(url);
            return true;
        }
        return false;
    }

    public String getNextURL() throws InterruptedException{
        return urlQueue.take();
        //could have used poll()
        //poll retrievs ans removes the head of the queue.if empty returns null immediately.
        //take waits until a URL is available, avoiding CPU time in busy-waiting. and also removes head.
    }

    public boolean isQueueEmpty() {
        return urlQueue.isEmpty();
    }
}
