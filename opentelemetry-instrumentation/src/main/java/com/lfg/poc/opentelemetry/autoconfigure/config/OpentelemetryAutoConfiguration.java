package com.lfg.poc.opentelemetry.autoconfigure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.exporters.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.exporters.logging.LoggingExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpansProcessor;
import io.opentelemetry.trace.Tracer;


@SuppressWarnings("deprecation")
@Configuration
//@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@ConditionalOnProperty(prefix = "lfg.opentelemetry.trace", name = "enabled", havingValue = "true")
public class OpentelemetryAutoConfiguration {

    @Autowired
    private OpentelemetryProperties opentelemetryProperties;
    
	@Bean
	public Tracer tracer() throws Exception {
		final Tracer tracer = OpenTelemetry.getTracerFactory().get(opentelemetryProperties.getTrace().getInstrumentationName());
		SpanProcessor logProcessor = SimpleSpansProcessor.newBuilder(new LoggingExporter()).build();
		OpenTelemetrySdk.getTracerFactory().addSpanProcessor(logProcessor);
		
		if(opentelemetryProperties.getJaeger().isEnabled())
			addJaeger();

		return tracer;
	}
	
	private void addJaeger() {
		
		SpanProcessor jaegerProcessor = SimpleSpansProcessor.newBuilder(JaegerGrpcSpanExporter.newBuilder()
				.setServiceName(opentelemetryProperties.getTrace().getServiceName() == null ? "UNKNOWN_SERVICE"
						: opentelemetryProperties.getTrace().getServiceName())
				.setChannel(ManagedChannelBuilder.forAddress(opentelemetryProperties.getJaeger().getHost(),
						opentelemetryProperties.getJaeger().getPort()).usePlaintext().build())
				.build()).build();
		OpenTelemetrySdk.getTracerFactory().addSpanProcessor(jaegerProcessor);

	}
	
	@Bean
	public OpentelemetryTraceInterceptor traceInterceptor(Tracer tracer) {
		return new OpentelemetryTraceInterceptor(tracer);
	}
	
	@Bean
	public WebMvcConfigurerAdapter interceptorConfig(OpentelemetryTraceInterceptor traceInterceptor) {
		return new WebMvcConfigurerAdapter() {
			@Override
		    public void addInterceptors(InterceptorRegistry registry){
		        registry.addInterceptor(traceInterceptor);
		    }
		};
	}
	
	@Bean
    @ConfigurationProperties(prefix = "lfg.opentelemetry")
    public OpentelemetryProperties opentelemetryProperties() {
        return new OpentelemetryProperties();
    }
}