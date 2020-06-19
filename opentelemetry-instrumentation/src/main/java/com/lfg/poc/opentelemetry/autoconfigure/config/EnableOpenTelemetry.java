package com.lfg.poc.opentelemetry.autoconfigure.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.lfg.poc.opentelemetry.autoconfigure.feign.FeignConfiguration;
import com.lfg.poc.opentelemetry.autoconfigure.prometheus.PrometheusConfiguration;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE })
@Documented
@Import({OpentelemetryAutoConfiguration.class, FeignConfiguration.class, PrometheusConfiguration.class})
public @interface EnableOpenTelemetry {
	
}