package com.ep.weare.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    로컬 서버용
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/announceImage/**")
                .addResourceLocations("file:///tomcat/webapps/announceImage/");

        registry.addResourceHandler("/kelly/**")
                .addResourceLocations("file:///tomcat/webapps/kelly/");

        registry.addResourceHandler("/announceAttach/**")
                .addResourceLocations("file:///tomcat/webapps/announceAttach/");

        registry.addResourceHandler("/questionImage/**")
                .addResourceLocations("file:///tomcat/webapps/questionImage/");

        registry.addResourceHandler("/ministryImage/**")
                .addResourceLocations("file:///tomcat/webapps/ministryImage/");

        registry.addResourceHandler("/executivesAttach/**")
                .addResourceLocations("file:///tomcat/webapps/executivesAttach/");
    }

    // 배포용
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/announceImage/**")
//                .addResourceLocations("file:///home/ubuntu/files/announceImage/");
//
//        registry.addResourceHandler("/kelly/**")
//                .addResourceLocations("file:///home/ubuntu/files/kelly/");
//
//        registry.addResourceHandler("/announceAttach/**")
//                .addResourceLocations("file:///home/ubuntu/files/announceAttach/");
//
//        registry.addResourceHandler("/questionImage/**")
//                .addResourceLocations("file:///home/ubuntu/files/questionImage/");
//
//        registry.addResourceHandler("/ministryImage/**")
//                .addResourceLocations("file:///home/ubuntu/files/ministryImage/");
//
//        registry.addResourceHandler("/executivesAttach/**")
//                .addResourceLocations("file:///home/ubuntu/files/executivesAttach/");
//    }
}
