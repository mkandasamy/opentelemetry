package com.lfg.poc.opentelemetry.productservice.dao;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lfg.poc.opentelemetry.dao.model.Item;

@FeignClient(value = "itemClient",
url = "${lfg.opentelemetry.service.item-service-url}",
fallback = ItemFallback.class)
public interface ItemClient {
 
    @RequestMapping(method = RequestMethod.GET, value = "/items/{id}", produces = "application/json")
    Item getById(@PathVariable("id") String id);
    
    @RequestMapping(method = RequestMethod.GET, value = "/items/search/findByName", produces = "application/json")
    List<Item> getByName(@RequestParam("name") String name);
    
    
    
}