package com.inu.inunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InunityServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InunityServerApplication.class, args);
	}

}
