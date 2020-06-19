package com.lfg.poc.opentelemetry.autoconfigure;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.lfg.poc.opentelemetry.autoconfigure.config.EnableOpenTelemetry;

import io.opentelemetry.trace.Tracer;

@EnableOpenTelemetry
@SpringBootTest
@TestPropertySource(properties = {"lfg.opentelemetry.trace.enabled=false", "lfg.opentelemetry.trace.instrumentation-name=com.lfg.opentelemetry"})
public class OpentelemetryDisabledTest {
	
	@Autowired(required = false)
	private Tracer tracer;

	@Test
	void traceNull() {
		assertNull(tracer, "tracer bean is not created");
	}
}