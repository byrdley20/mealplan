package com.meals;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan
@EnableJpaRepositories
@EnableAutoConfiguration
@SpringBootApplication
public class MealPlanApplication extends SpringBootServletInitializer {

	private static Class<MealPlanApplication> applicationClass = MealPlanApplication.class;

	public static void main(String[] args) throws IOException {
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
		SpringApplication.run(applicationClass, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}

}