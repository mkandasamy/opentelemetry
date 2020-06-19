package com.lfg.poc.opentelemetry.autoconfigure.feign;

//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.opentelemetry.context.propagation.HttpTextFormat;
//import io.opentelemetry.trace.AttributeValue;
//import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.SpanContext;
import io.opentelemetry.trace.Tracer;

public class FeignClientInterceptor implements RequestInterceptor {

	@Autowired
	Tracer tracer;

	@Override
	public void apply(RequestTemplate requestTemplate) {

		SpanContext spanContext = tracer.getCurrentSpan().getContext();

		tracer.getHttpTextFormat().inject(spanContext, requestTemplate, new HttpTextFormat.Setter<RequestTemplate>() {
			@Override
			public void put(RequestTemplate template, String key, String value) {
				template.header(key, value);
			}
		});

		/*Span currentSpan = tracer.spanBuilder(Span.Kind.CLIENT.name() + " " + requestTemplate.method() + " " + requestTemplate.path()).setParent(spanContext)
				.setSpanKind(Span.Kind.CLIENT).startSpan();
		
		Map<String, AttributeValue> eventAttributes = new HashMap<>();
		eventAttributes.put("url", AttributeValue.stringAttributeValue(requestTemplate.feignTarget().url()));
		eventAttributes.put("method", AttributeValue.stringAttributeValue(requestTemplate.method()));
		currentSpan.addEvent("Request Raised", eventAttributes);
		
		currentSpan.setAttribute("location", "FeignClientInterceptor.apply");
		currentSpan.setAttribute("http.source", AttributeValue.stringAttributeValue(requestTemplate.feignTarget().name()));
		currentSpan.setAttribute("http.url", AttributeValue.stringAttributeValue(requestTemplate.feignTarget().url()));
		currentSpan.setAttribute("http.path", AttributeValue.stringAttributeValue(requestTemplate.path()));
		currentSpan.setAttribute("http.method", AttributeValue.stringAttributeValue(requestTemplate.method()));
		currentSpan.setAttribute("http.query", AttributeValue.stringAttributeValue(requestTemplate.queryLine()));
		currentSpan.setAttribute("http.headers", 
				AttributeValue.stringAttributeValue(requestTemplate.headers().keySet().stream().map(e -> e + ": " + requestTemplate.headers().get(e).stream().collect(Collectors.joining(", ")))
				.collect(Collectors.joining("\n"))));
		currentSpan.end();*/
	}
}