package com.lfg.poc.opentelemetry.autoconfigure.feign;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.okhttp.OkHttpClient;

@Configuration
@ConditionalOnProperty(prefix = "lfg.opentelemetry.feign", name = "enabled", havingValue = "true")
public class FeignConfiguration {
	
	@Bean
	public FeignClientInterceptor feignClientInterceptor() {
		return new FeignClientInterceptor();
	}
	
	@Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

}
