package com.lfg.poc.opentelemetry.reviewservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import com.lfg.poc.opentelemetry.autoconfigure.config.EnableOpenTelemetry;
import com.lfg.poc.opentelemetry.dao.config.EnableElasticSearch;

@SpringBootApplication
@EnableOpenTelemetry
@EnableFeignClients
@EnableElasticSearch
public class Application  {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(Application.class);
        application.addInitializers(new Application.ServicesApplicationContextInitializer());
        application.run(args);
    }
    
    public static class ServicesApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        	
        }
    }
}


