package com.lfg.poc.opentelemetry.productservice.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lfg.poc.opentelemetry.dao.model.Item;

@Component
public class ItemFallback implements ItemClient {

	@Override
	public Item getById(String id) {
		return null;
	}
	
	public List<Item> getByName(String name) {
		return Collections.emptyList();
	}
}