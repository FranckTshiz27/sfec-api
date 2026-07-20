package com.rawsur.apidgi;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class ApiDgiApplication implements CommandLineRunner {

	
	public static void main(String[] args) {
		SpringApplication.run(ApiDgiApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Serveur run");
	}

	

}
