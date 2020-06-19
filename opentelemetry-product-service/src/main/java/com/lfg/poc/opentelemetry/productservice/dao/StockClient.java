package com.lfg.poc.opentelemetry.productservice.dao;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lfg.poc.opentelemetry.dao.model.Stock;

@FeignClient(value = "stockClient",
url = "${lfg.opentelemetry.service.stock-service-url}",
fallback = StockFallback.class)
public interface StockClient {
 
    @RequestMapping(method = RequestMethod.GET, value = "/stocks/{id}", produces = "application/json")
    Stock getById(@PathVariable("id") String id);
    
    @RequestMapping(method = RequestMethod.GET, value = "/stocks/search/findByName", produces = "application/json")
    List<Stock> getByName(@RequestParam("name") String name);
}