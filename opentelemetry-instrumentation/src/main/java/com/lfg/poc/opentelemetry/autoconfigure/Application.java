package com.lfg.poc.opentelemetry.autoconfigure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.lfg.poc.opentelemetry.autoconfigure.config.EnableOpenTelemetry;

@SpringBootApplication
@EnableFeignClients
@EnableOpenTelemetry
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}