package com.lfg.poc.opentelemetry.autoconfigure.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import io.opentelemetry.context.propagation.HttpTextFormat;
import io.opentelemetry.trace.AttributeValue;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.SpanContext;
import io.opentelemetry.trace.Tracer;

public class OpentelemetryTraceInterceptor implements HandlerInterceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(OpentelemetryTraceInterceptor.class);

	private Tracer tracer;

	public OpentelemetryTraceInterceptor(Tracer tracer) {
		this.tracer = tracer;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpTextFormat<SpanContext> textFormat = tracer.getHttpTextFormat();
		Span span;
		try {
			SpanContext spanContext = textFormat.extract(request, new HttpTextFormat.Getter<HttpServletRequest>() {
				@Override
				public String get(HttpServletRequest req, String key) {
					return req.getHeader(key);
				}
			});
			span = tracer.spanBuilder(Span.Kind.SERVER.name() + " " + request.getMethod() + " " + request.getRequestURI()).setParent(spanContext)
					.setSpanKind(Span.Kind.SERVER).startSpan();
			
			String clientIp = request.getHeader("X-FORWARDED-FOR"); 
			
			Map<String, AttributeValue> eventAttributes = new HashMap<>();
			eventAttributes.put("client-ip", AttributeValue.stringAttributeValue(clientIp == null ? request.getRemoteAddr() : clientIp));
			eventAttributes.put("scheme", AttributeValue.stringAttributeValue(request.getScheme()));
			span.addEvent("Request Received", eventAttributes);

			span.setAttribute("location", "OpentelemetryTraceInterceptor.preHandle");
			span.setAttribute("http.url", request.getRequestURL().toString());
			span.setAttribute("http.path", request.getRequestURI());
			span.setAttribute("http.method", request.getMethod());
			span.setAttribute("http.query", request.getQueryString() == null ? "" : request.getQueryString());
			
			String headers = Collections.list(request.getHeaderNames()).stream()
					.map(e -> e + ": " + Collections.list(request.getHeaders(e)).stream().collect(Collectors.joining(", ")))
					.collect(Collectors.joining("\n"));
			span.setAttribute("http.headers", AttributeValue.stringAttributeValue(headers));

		} catch (Exception e) {
			span = tracer.spanBuilder(request.getRequestURI()).startSpan();
			span.addEvent("Request Error");
			span.setAttribute("location", "OpentelemetryTraceInterceptor.preHandle");
			span.addEvent(e.toString());
			span.setAttribute("error", true);
		}
		tracer.withSpan(span);
		logger.info("Pre Handle Called");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		HttpTextFormat<SpanContext> textFormat = tracer.getHttpTextFormat();
		Span currentSpan = tracer.getCurrentSpan();
		Map<String, AttributeValue> eventAttributes = new HashMap<>();
		eventAttributes.put("status", AttributeValue.longAttributeValue(response.getStatus()));
		//eventAttributes.put("content-type", AttributeValue.stringAttributeValue(response.getContentType()));
		currentSpan.addEvent("Response Returned", eventAttributes);
		currentSpan.setAttribute("location", "OpentelemetryTraceInterceptor.postHandle");
		textFormat.inject(currentSpan.getContext(), response, new HttpTextFormat.Setter<HttpServletResponse>() {
			@Override
			public void put(HttpServletResponse response, String key, String value) {
				response.addHeader(key, value);
			}
		});
		currentSpan.end();
		logger.info("Post Handler Called");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {
	}
}
