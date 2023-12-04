package com.ep.weare.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/announceImage/**")
                .addResourceLocations("file:///C:/weareAttach/announceImage/");

        registry.addResourceHandler("/kelly/**")
                .addResourceLocations("file:///C:/weareAttach/kelly/");

        registry.addResourceHandler("/announceAttach/**")
                .addResourceLocations("file:///C:/weareAttach/announceAttach/");

        registry.addResourceHandler("/questionImage/**")
                .addResourceLocations("file:///C:/weareAttach/questionImage/");

        registry.addResourceHandler("/ministryImage/**")
                .addResourceLocations("file:///C:/weareAttach/ministryImage/");

        registry.addResourceHandler("/executivesAttach/**")
                .addResourceLocations("file:///C:/weareAttach/executivesAttach/");
    }
}
