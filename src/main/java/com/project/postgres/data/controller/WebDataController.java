package com.project.postgres.data.controller;

import static org.mockito.Matchers.intThat;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.postgres.data.crawling.Crawler;
import com.project.postgres.data.crawling.util.SameWebsiteOnlyFilter;
import com.project.postgres.data.service.WebDataService;

@Controller
public class WebDataController {
	
	private static final Logger log = LogManager.getLogger(WebDataController.class);
	
	@Autowired
	WebDataService service;
	
	@Autowired
	Crawler crawler;
	
	@RequestMapping(value="main/main", method={RequestMethod.POST, RequestMethod.GET})
	public String main(HashMap<String,Object> pMap, Model model) throws Exception{
		pMap.put("fName", "3");
		model.addAttribute("now", service.now(pMap) );
		model.addAttribute("list", service.list(pMap) );
		return "main/main";
	}
	
	@RequestMapping(value="main/craw", method={RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public void crawl(@RequestParam(name="url"
									, defaultValue="http://www.ygosu.com/community/yeobgi"
									, required=false) String url
					   ,@RequestParam(name="size"
					   				, defaultValue="10") int size
									, Model model
									, HttpServletRequest req
									, HttpServletResponse res
									, ArrayList<String> imgList
			) throws Exception{
		
		log.info("=========================================== START");
		log.info("|    URL :: " + url + "    |");
		log.info("=========================================== END");

		//THIS IS CRAW
        crawler.setUrlFilter(new SameWebsiteOnlyFilter(url));
        crawler.setPageProcessor(null); // set an IPageProcessor instance here.
        crawler.addUrl(url);
        crawler.crawl(size, imgList);
		
        PrintWriter out = res.getWriter();
        out.print("<html> <head><title></title><head>");
        out.println(size);
		out.println("<p> It's Done</p> \n" + "<p>COMPLETE : " + size + "<p>" );
		for(String imgSrc : imgList){
			out.println("<img src=\"" + imgSrc + "\">");
		}
		out.print("</html>");
		
	}
	
}
