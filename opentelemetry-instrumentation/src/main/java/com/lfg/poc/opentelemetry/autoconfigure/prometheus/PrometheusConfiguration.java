package com.lfg.poc.opentelemetry.autoconfigure.prometheus;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.MetricsServlet;

@Configuration
@ConditionalOnProperty(prefix = "lfg.opentelemetry.prometheus", name = "enabled", havingValue = "true")
public class PrometheusConfiguration {

     @Bean
     @ConditionalOnMissingBean
     public CollectorRegistry metricRegistry() {
         return CollectorRegistry.defaultRegistry;
     }

     @Bean
     public ServletRegistrationBean<MetricsServlet> registerPrometheusExporterServlet(CollectorRegistry metricRegistry) {
           return new ServletRegistrationBean<MetricsServlet>(new MetricsServlet(metricRegistry), "/metrics");
     }
}