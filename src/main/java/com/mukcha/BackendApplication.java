package com.mukcha;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class BackendApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "/app/config/mukcha/real-application.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(BackendApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args)
		;
		// SpringApplication.run(BackendApplication.class, args);
	}

}
