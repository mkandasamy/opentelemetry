package com.lfg.poc.opentelemetry.productservice.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lfg.poc.opentelemetry.dao.model.Stock;

@Component
public class StockFallback implements StockClient {

	@Override
	public Stock getById(String id) {
		return null;
	}
	
	public List<Stock> getByName(String name) {
		return Collections.emptyList();
	}
}