package com.veragg.website.jobs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CrawlerJobWrapper<T extends CrawlerJob<?>> {

    @NonNull
    private final T job;

    @NonNull
    private final CrawlerJobConfigurations.CrawlerJobConfiguration configuration;

}
