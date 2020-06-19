package com.lfg.poc.opentelemetry.clientservice.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lfg.poc.opentelemetry.clientservice.dao.ProductClient;
import com.lfg.poc.opentelemetry.dao.model.Product;

import io.opentelemetry.context.Scope;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Tracer;

@RestController
@RequestMapping("/")
public class HitController {

	private static final Logger logger = LoggerFactory.getLogger(HitController.class);

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

	@GetMapping("/hits/{count}")
	public String hit(@PathVariable(required = false, name = "count") Integer count) {
		
		tracer.getCurrentSpan().end();
		
		int max = Math.min(1000, count);
		
		IntStream.range(0, max).forEach(i -> hitProductRequestById(i));
		
		return "endpoint http://" + getPrivateIp() + ":8080/products/1 is hit " + max + " times";
	}
	
	private static String getPrivateIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress().trim();
		} catch (UnknownHostException e) {
			return "localhost";
		}
	}
}