package com.DistanceGenerator.DistanceGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
public class DistanceGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistanceGeneratorApplication.class, args);
	}
     @Bean
	 public RestTemplate getRestTemplate() {
		return new RestTemplate();
	 }
}
