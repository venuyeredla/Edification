package org.edification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Edification{
	private static final Logger LOGGER = LoggerFactory.getLogger(Edification.class);

	public static void main(String[] args) {
		SpringApplication.run(Edification.class, args);
		LOGGER.info("Started application");
	}
}
