package com.microcode.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class ClientApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		SpringApplication.run(ClientApplication.class, args);
	}

}