package com.lfg.poc.opentelemetry.clientservice;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lfg.poc.opentelemetry.clientservice.dao.ProductClient;
import com.lfg.poc.opentelemetry.dao.model.Product;

import io.opentelemetry.context.Scope;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Tracer;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
	
    private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);
    
    @Override
    public void run(String...args) throws Exception {
        logger.info("Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.", Arrays.toString(args));
        
        IntStream.range(0, 100).forEach(i -> hitProductRequestById(i));
    }
    
	@Autowired
	private ProductClient productClient;

	@Autowired
	private Tracer tracer;

	@Autowired
	private Environment environment;
	
	
	private void hitProductRequestById(int i) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Span span = tracer.spanBuilder(environment.getProperty("lfg.opentelemetry.trace.service-name"))
				.setSpanKind(Span.Kind.CLIENT).startSpan();
		span.addEvent("Start get-product-by-id");
		span.setAttribute("level", "INFO");

		try (Scope scope = tracer.withSpan(span)) {
			logger.info(tracer.getCurrentSpan().toString());
			Product product = productClient.getById("1");
			System.out.println(gson.toJson(product));
			System.out.println("--------------- " + i + " ---------------");
		} catch (Exception e) {
			span.addEvent("error");
			span.setAttribute("message", e.getMessage());
			span.setAttribute("level", "ERROR");
		} finally {
			span.end();
		}
	}

}