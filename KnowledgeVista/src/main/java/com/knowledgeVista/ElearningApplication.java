package com.knowledgeVista;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = {
    "com.knowledgeVista",
    "com.knowledgeVista.config",
    "com.knowledgeVista.User",
    "com.knowledgeVista.Course",
    "com.knowledgeVista.License",
    "com.knowledgeVista.Email",
    "com.knowledgeVista.FileService"
})
public class ElearningApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ElearningApplication.class, args);
		
	}
	  @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(ElearningApplication.class);
	    }

}
