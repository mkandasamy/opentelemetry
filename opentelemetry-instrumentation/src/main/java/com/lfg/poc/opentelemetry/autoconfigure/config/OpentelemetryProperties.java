package com.lfg.poc.opentelemetry.autoconfigure.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class OpentelemetryProperties {

    private Trace trace = new Trace();
    private Feign feign = new Feign();
    private Prometheus prometheus = new Prometheus();
    private Jaeger jaeger = new Jaeger();
    private Service service = new Service();
    
    
    @Getter @Setter @NoArgsConstructor
    public class Feign {
    	public boolean enabled;
    }
    
    @Getter @Setter @NoArgsConstructor
    public class Prometheus {
    	public boolean enabled;
    }
    
    @Getter @Setter @NoArgsConstructor
    public class Jaeger {
    	public boolean enabled;
    	public String host = getPrivateIp();
    	public Integer port = 14250;
    }
    
    @Getter @Setter @NoArgsConstructor
    public class Trace {
    	public boolean enabled;
    	private String serviceName;
        private String instrumentationName = "lfg.com.opentelemetry";
    }
    
    @Getter @Setter @NoArgsConstructor
    public class Service{
    	private String productServiceUrl;
    	private String itemServiceUrl;
    	private String reviewServiceUrl;
    	private String stockServiceUrl;
    	private String elasticSearchUrl;
    }
    
    private static String getPrivateIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress().trim();
		} catch (UnknownHostException e) {
			return "localhost";
		}
	}
}