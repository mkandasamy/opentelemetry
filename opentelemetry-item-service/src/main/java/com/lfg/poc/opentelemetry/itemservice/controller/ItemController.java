package com.lfg.poc.opentelemetry.itemservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lfg.poc.opentelemetry.dao.model.Item;
import com.lfg.poc.opentelemetry.dao.model.Review;
import com.lfg.poc.opentelemetry.itemservice.dao.ReviewClient;

@RestController
@RequestMapping("/")
public class ItemController {

	private ElasticsearchOperations elasticsearchOperations;
	
	@Autowired 
	private ReviewClient reviewClient;

	public ItemController(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@GetMapping("/items/{id}")
	public Item findById(@PathVariable("id") String id) {
		Item item = elasticsearchOperations.get(id, Item.class);
		item.setReview(reviewClient.getById(id));
		return item;
	}

	@GetMapping("/items/search/findByName")
	public List<Item> findByName(@RequestParam("name") String name,
			@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
				.withQuery(QueryBuilders.matchQuery("name", name)).build();

		SearchHits<Item> hits = elasticsearchOperations.search(searchQuery, Item.class);
		return hits.get().map(m -> {
			Item i = m.getContent();
			Review review = reviewClient.getById(i.getId() + "");
			i.setReview(review);
			return i;
		}).collect(Collectors.toList());

	}

}