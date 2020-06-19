package com.lfg.poc.opentelemetry.productservice.controller;

/*import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestParam;
*/
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.lfg.poc.opentelemetry.dao.model.Item;
import com.lfg.poc.opentelemetry.dao.model.Product;
import com.lfg.poc.opentelemetry.dao.model.Stock;
import com.lfg.poc.opentelemetry.productservice.dao.ItemClient;
import com.lfg.poc.opentelemetry.productservice.dao.StockClient;

@RestController
public class ProductController {

	@Autowired
	private ItemClient itemClient;

	@Autowired
	private StockClient stockCliient;

	@RequestMapping("/products/{id}")
	public Product getProduct(@PathVariable String id) {
		Item item = itemClient.getById(id);
		List<Stock> stocks = stockCliient.getByName(item.getName());
		return new Product(id, item, stocks);
	}
	
	/*
	
	@RequestMapping("/products/search/findByName")
	public List<Product> getProducts(@RequestParam("name") String name) {
		
		try {
			return collectProducts(name);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		
	}
	
	private List<Product> collectProducts(String name) throws InterruptedException, ExecutionException{
		return CompletableFuture.supplyAsync(() -> {
			return itemClient.getByName(name);
		}).thenCombine(CompletableFuture.supplyAsync(() -> {
			return stockCliient.getByName(name);
		}), (items, stocks) -> {
			return items.stream().map(i -> new Product(i.getId(), i, stocks)).collect(Collectors.toList());
		}).exceptionally(e -> {
			e.printStackTrace();
			return Collections.emptyList();
		}).get();
	} 
	
	*/
}
