package com.rlabs.merkuri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Merkuri Mail Server with Message Broker.
 *
 * @author Ryan Padilha <ryan.padilha@gmail.com>
 * @since 0.0.1
 *
 */
@SpringBootApplication
public class MerkuriMailServerApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(MerkuriMailServerApplication.class);

	public static void main(String[] args) {
		LOGGER.info("Running Merkuri Application");
		SpringApplication.run(MerkuriMailServerApplication.class, args);
	}
}
