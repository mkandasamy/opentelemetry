package com.lfg.poc.opentelemetry.clientservice.dao;

import org.springframework.stereotype.Component;

import com.lfg.poc.opentelemetry.dao.model.Product;

@Component
public class ProductFallback implements ProductClient {

	@Override
	public Product getById(String id) {
		return null;
	}
	
}