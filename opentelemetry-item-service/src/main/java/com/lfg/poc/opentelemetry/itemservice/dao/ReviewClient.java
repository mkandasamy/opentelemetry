package com.lfg.poc.opentelemetry.itemservice.dao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lfg.poc.opentelemetry.dao.model.Review;

@FeignClient(value = "reviewsClient",
url = "${lfg.opentelemetry.service.review-service-url}",
fallback = ReviewFallback.class)
public interface ReviewClient {
 
    @RequestMapping(method = RequestMethod.GET, value = "/reviews/{id}", produces = "application/json")
    Review getById(@PathVariable("id") String id);
}