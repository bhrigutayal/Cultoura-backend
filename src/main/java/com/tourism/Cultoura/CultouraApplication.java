package com.tourism.Cultoura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tourism.Cultoura", "com.tourism.Cultoura.service"})
public class CultouraApplication {

	public static void main(String[] args) {
		SpringApplication.run(CultouraApplication.class, args);
	}

}
