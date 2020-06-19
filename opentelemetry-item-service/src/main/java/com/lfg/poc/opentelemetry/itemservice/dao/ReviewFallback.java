package com.lfg.poc.opentelemetry.itemservice.dao;

import org.springframework.stereotype.Component;

import com.lfg.poc.opentelemetry.dao.model.Review;

@Component
public class ReviewFallback implements ReviewClient {

	@Override
	public Review getById(String id) {
		return null;
	}
}