package com.tourism.Cultoura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.tourism.Cultoura", "com.tourism.Cultoura.service"})
public class CultouraApplication {

	public static void main(String[] args) {
		SpringApplication.run(CultouraApplication.class, args);
	}

}
