package com.project.postgres.data.crawling;

import org.jsoup.nodes.Document;

/**
 */
public interface IPageProcessor {
    public void process(String url, Document doc);
}
