package com.project.postgres.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class DatasupplyprojectApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DatasupplyprojectApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(DatasupplyprojectApplication.class, args);
	}

	
}
