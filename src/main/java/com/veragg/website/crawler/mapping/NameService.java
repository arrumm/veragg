package com.veragg.website.crawler.mapping;

import org.springframework.stereotype.Service;

@Service
public class NameService {

    public String normalize(String name) {

        String result = name;
        result = result.replace("ü", "u");
        result = result.replace("ö", "o");
        result = result.replace("ä", "a");
        result = result.replace(" -", "-");
        result = result.replace("- ", "-");
        result = result.replace("ß", "ss");

        return result;

    }

}
