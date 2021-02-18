package com.game.b1ingservice;

import com.game.b1ingservice.config.AMBProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({AMBProperty.class})
@SpringBootApplication
public class B1ingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(B1ingServiceApplication.class, args);
	}



}
