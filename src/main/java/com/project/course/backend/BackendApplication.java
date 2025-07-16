package com.project.course.backend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class BackendApplication implements CommandLineRunner {

	private final Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args){
		String port = environment.getProperty("server.port", "8000");
		String contextPath = environment.getProperty("server.servlet.context-path", "");
		String swaggerUrl = "http://localhost:" + port + contextPath + "/swagger-ui.html";
		log.info("Swagger UI is available at: {}", swaggerUrl);
	}

}
