package com.lfg.poc.opentelemetry.dao.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "item")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

	@JSONField(name = "id") @Field("id")
    private Integer id;
	@JSONField(name = "name") @Field("name")
    private String name;
	@JSONField(name = "price") @Field("price")
    private Double price;
	private Review review;
}
