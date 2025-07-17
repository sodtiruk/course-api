package com.project.course.backend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class BackendApplication {

	private final Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			String port = environment.getProperty("server.port", "8000");
			String contextPath = environment.getProperty("server.servlet.context-path", "");
			String swaggerUrl = "http://localhost:" + port + contextPath + "/swagger-ui.html";
			log.info("Swagger UI is available at: {}", swaggerUrl);

		};
	}

}
