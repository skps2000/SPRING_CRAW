package com.project.postgres.data.crawling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.postgres.data.crawling.util.IUrlFilter;
import com.project.postgres.data.crawling.util.UrlNormalizer;

/**
 */
public class Crawler {

	private static final Logger log = LogManager.getLogger(Crawler.class);
	
    protected IUrlFilter urlFilter     = null;
    protected List<String> urlsToCrawl = new ArrayList<String>();
    protected Set<String>  crawledUrls = new HashSet<String>();
    protected IPageProcessor pageProcessor = null;
    
    public Crawler() {
    }

    public void setUrlFilter(IUrlFilter urlFilter) {
        this.urlFilter = urlFilter;
    }

    public void setPageProcessor(IPageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    public void addUrl(String url) {
        this.urlsToCrawl.add(url);
    }
    
    public int crawl(int limitSize, ArrayList<String> imgList) {

    	long curSize = 0;
        long startTime = System.currentTimeMillis();

        while(this.urlsToCrawl.size() > 0) {
            String nextUrl = this.urlsToCrawl.remove(0);
            if (!shouldCrawlUrl(nextUrl)) continue; // skip this URL.
            this.crawledUrls.add(nextUrl);
            try {
            	log.info( curSize + " PROCESSING...  " + "URL ::: " + nextUrl);
                CrawlJob crawlJob = new CrawlJob(nextUrl, this.pageProcessor);
                crawlJob.addPageProcessor(new IPageProcessor() {
                    @Override
                    public void process(String url, Document doc) {
                    	
                    	HashMap<String, Object> pMap = new HashMap<String,Object>();
                    	
                    	String subJect = doc.select("div.board_top div.tit h3").text().toString();
                        subJect = subJect.replaceAll("\\[[0-9]*\\]", "");
                        subJect = subJect.replaceAll("^[0-9]*\\s", "");
                        
                        String writer = doc.select("div.board_body div.nickname a").text().toString();
                        String contents = doc.select("div.container").text().toString();
                        String date = doc.select("div.board_body div.date").text().toString().replaceAll("/ READ.*", "");
                        
                        log.info( writer );
                        log.info( subJect );
                        log.info( contents );
                        log.info( date );
                        
                        pMap.put("writer", writer.trim());
                        pMap.put("subject", subJect.trim());
                        pMap.put("contents", contents.trim());
                        pMap.put("date", date.trim());
                        pMap.put("original", url);
                        
                        Elements images = doc.select("div.container img");
                        for(Element image : images){
                        	log.info(image.attr("src"));
                        	pMap.put("fileName", image.attr("src").toString());
                        }
                        
                        Elements elements = doc.select("a");
                        String baseUrl = url;
                        for(Element element : elements){
                            String linkUrl       = element.attr("href");
                            String normalizedUrl = UrlNormalizer.normalize(linkUrl, baseUrl);
                            addUrl(normalizedUrl);
                        }
                        
                    }
                });
                
                curSize ++;
                if(curSize >= limitSize) break;
                
                crawlJob.crawl();
                
            } catch (Exception e) {
            	log.info("Error crawling URL: " + nextUrl);
                e.printStackTrace();
            }

        }
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        log.info("URL's crawled: " + this.crawledUrls.size() + " in " + totalTime + " ms (avg: " + totalTime / this.crawledUrls.size() + ")");
        
        return limitSize;

    }

    private boolean shouldCrawlUrl(String nextUrl) {
        if(this.urlFilter != null && !this.urlFilter.include(nextUrl)){
            return false;
        }
        if(this.crawledUrls.contains(nextUrl)) { return false; }
        if(nextUrl.startsWith("javascript:"))  { return false; }
        if(nextUrl.contains("mailto:"))        { return false; }
        if(nextUrl.startsWith("#"))            { return false; }
        if(nextUrl.endsWith(".swf"))           { return false; }
        if(nextUrl.endsWith(".pdf"))           { return false; }
        if(nextUrl.endsWith(".png"))           { return false; }
        if(nextUrl.endsWith(".gif"))           { return false; }
        if(nextUrl.endsWith(".jpg"))           { return false; }
        if(nextUrl.endsWith(".jpeg"))          { return false; }

        return true;
    }


}
