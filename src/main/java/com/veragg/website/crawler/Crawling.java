package com.veragg.website.crawler;

import java.util.Set;

public interface Crawling {

    Set<String> collectUrls(String startUrl, int startDepth, int endDepth);

}
