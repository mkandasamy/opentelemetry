package com.lfg.poc.opentelemetry.autoconfigure;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.lfg.poc.opentelemetry.autoconfigure.config.EnableOpenTelemetry;

import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.trace.Tracer;

@EnableOpenTelemetry
@SpringBootTest
@TestPropertySource(properties = {"lfg.opentelemetry.trace.enabled=true", "lfg.opentelemetry.trace.instrumentation-name=com.lfg.opentelemetry"})
public class OpentelemetryEnabledTest {
	
	@Autowired(required = false)
	private Tracer tracer;

	@Test
	void tracerNotNull() {
		assertNotNull(tracer, "tracer bean is created");
	}
	
	@Test
	void instrumentationIsCorrect() {
		ReadableSpan readableSpan = (ReadableSpan) tracer.spanBuilder("sample-service").startSpan();
		assertEquals("com.lfg.opentelemetry", 
				readableSpan.getInstrumentationLibraryInfo().name(), "instrumentation-name is correct");
	}

}