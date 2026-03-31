package com.sathwik.auth.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AuthServiceApplication {
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("DRIVER FOUND!");
		} catch (ClassNotFoundException e) {
			System.out.println("DRIVER NOT FOUND: " + e.getMessage());
		}
		SpringApplication.run(AuthServiceApplication.class, args);
	}
}
