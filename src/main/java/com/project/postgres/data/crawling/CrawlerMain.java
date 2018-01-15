package com.project.postgres.data.crawling;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.project.postgres.data.crawling.util.SameWebsiteOnlyFilter;

/**
 * This class is an example of how to use the Crawler class. You should
 * not expect to use this class as it is. Use the Crawler class directly
 * from your own code.
 */
public class CrawlerMain {

	/****
	 * START TO CRAWLER     	
	 */
//    public static void main(String[] args) {
//        String url = "http://www.ygosu.com/community/yeobgi/";
//        Crawler crawler  = new Crawler();
//        crawler.setUrlFilter(new SameWebsiteOnlyFilter(url));
//        crawler.setPageProcessor(null); // set an IPageProcessor instance here.
//        crawler.addUrl(url);
//
//        crawler.crawl();
//    	
//    }
    
    public void main(String url){
        Crawler crawler  = new Crawler();
        crawler.setUrlFilter(new SameWebsiteOnlyFilter(url));
        crawler.setPageProcessor(null); // set an IPageProcessor instance here.
        crawler.addUrl(url);
        crawler.crawl();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void jsoupWebScrapping(){

    	/***
    	 * JSOUP
    	 * 
    	 * 
    	 */
    	
    	String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";

    	Map<String, String> data = new HashMap<>();
    	data.put("login_id", "jsg1988");
    	data.put("login_pwd", "123123");
    	data.put("rememberLoginId", "1");
    	data.put("redirectUrl", "http://tistory.com/");

    	Connection.Response response = null;
		try {
			response = Jsoup.connect("http://www.ygosu.com/login/login.yg")
			                                    .userAgent(userAgent)
			                                    .timeout(3000)
			                                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
			                                    .header("Content-Type", "application/x-www-form-urlencoded")
			                                    .header("Accept-Encoding", "gzip, deflate, br")
			                                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
			                                    .data(data)
			                                    .method(Connection.Method.POST)
			                                    .execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

    	Map<String, String> loginCookie = response.cookies();
    	
    	Document adminPageDocument = null;
		try {
			adminPageDocument = Jsoup.connect("http://www.ygosu.com/community/adultpic/29003")
			                            .userAgent(userAgent)
			                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
			                            .header("Content-Type", "application/x-www-form-urlencoded")
			                            .header("Accept-Encoding", "gzip, deflate, sdch")
			                            .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
			                            .cookies(loginCookie) // ������ ���� '�α��� ��' ��Ű
			                            .get();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		System.out.println(adminPageDocument);
		
    	// select ���� option �±� ��ҵ�?
//    	Elements blogOptions = adminPageDocument.select("div.board_body");
    	Elements blogOptions = adminPageDocument.select("div.board_body div.container table tbody tr td");

    	System.out.println(blogOptions.toString());
    	
    	// ��α�? �̸��� url ����?
    	for(Element option : blogOptions) {
    	  String blogName = option.text();
    	  String blogUrl = option.attr("abs:value");
    	  
//    	  System.out.println(blogName); // ������ ��α�?
//    	  System.out.println(blogUrl); // http://partnerjun.tistory.com/admin/center/
    	}
    	
    	
    }
    
    
    
}
