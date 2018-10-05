package com.example.springreactive;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

@EnableWebFlux
@SpringBootApplication
public class SpringReactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveApplication.class, args);
	}


	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().registerModule(new KotlinModule());
	}
}
