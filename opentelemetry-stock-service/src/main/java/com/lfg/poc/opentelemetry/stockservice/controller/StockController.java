package com.lfg.poc.opentelemetry.stockservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.QueryBuilders;
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

import com.lfg.poc.opentelemetry.dao.model.Stock;

@RestController
@RequestMapping("/")
public class StockController {

	private ElasticsearchOperations elasticsearchOperations;

	public StockController(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@GetMapping("/stocks/{id}")
	public Stock findById(@PathVariable("id") String id) {
		return elasticsearchOperations.get(id, Stock.class);
	}

	@GetMapping("/stocks/search/findByName")
	public List<Stock> findByLastName(@RequestParam("name") String name,
			@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize) {
		Pageable pageable = PageRequest.of(0, 10);

		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
				.withQuery(QueryBuilders.matchQuery("name", name)).build();

		SearchHits<Stock> hits = elasticsearchOperations.search(searchQuery, Stock.class);
		return hits.get().map(m -> m.getContent()).collect(Collectors.toList());

	}

}