package com.lfg.poc.opentelemetry.clientservice.dao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lfg.poc.opentelemetry.dao.model.Product;

@FeignClient(value = "productClient",
url = "${lfg.opentelemetry.service.product-service-url}",
fallback = ProductFallback.class)
public interface ProductClient {
 
    @RequestMapping(method = RequestMethod.GET, value = "/products/{id}", produces = "application/json")
    Product getById(@PathVariable("id") String id);
    
}