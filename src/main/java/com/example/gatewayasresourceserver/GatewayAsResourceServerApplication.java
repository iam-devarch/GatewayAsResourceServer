package com.example.gatewayasresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class GatewayAsResourceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayAsResourceServerApplication.class, args);
	}

}
  