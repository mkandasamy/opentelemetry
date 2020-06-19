package com.lfg.poc.opentelemetry.dao.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class ESRestClientConfig extends AbstractElasticsearchConfiguration {
	
	@Autowired
	private Environment environment;
	
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()  
            .connectedTo(getEsUrl())
            .build();

        return RestClients.create(clientConfiguration).rest();          
    }
    
    private String getEsUrl() {
    	String host = environment.getProperty("LOCALHOST_IP", getPrivateIp());
    	String port = environment.getProperty("lfg.opentelemetry.service.elastic-search-port", "9200");
    	String url = environment.getProperty("lfg.opentelemetry.service.elastic-search-url", host + ":" + port);
    	
    	System.out.println("-------------------------------");
    	System.out.println("ES url: " + url);
    	System.out.println("-------------------------------");
    	
    	return url;
    		
    }
    
    private static String getPrivateIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress().trim();
		} catch (UnknownHostException e) {
			return "localhost";
		}
	}
    
}