package com.game.b1ingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class B1ingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(B1ingServiceApplication.class, args);
	}



}
