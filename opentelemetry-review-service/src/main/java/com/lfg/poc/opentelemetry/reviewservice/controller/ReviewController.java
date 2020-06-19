package com.lfg.poc.opentelemetry.reviewservice.controller;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lfg.poc.opentelemetry.dao.model.Review;


@RestController
@RequestMapping("/")
public class ReviewController {

	private ElasticsearchOperations elasticsearchOperations;

	public ReviewController(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@GetMapping("/reviews/{id}")
	public Review findById(@PathVariable("id") String id) {
		return elasticsearchOperations.get(id, Review.class);
	}
}