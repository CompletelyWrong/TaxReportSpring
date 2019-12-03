package com.project.reportsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
public class ReportSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportSystemApplication.class, args);
	}

}
