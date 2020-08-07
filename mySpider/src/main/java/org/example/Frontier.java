package org.example;

public interface Frontier {
    public CrawlUrl getNext() throws Exception;
    public boolean putUrl(CrawlUrl url);
    //public boolean visited(CrawlUrl);
}
