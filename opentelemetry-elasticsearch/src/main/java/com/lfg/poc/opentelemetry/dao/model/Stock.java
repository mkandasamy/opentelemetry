package com.lfg.poc.opentelemetry.dao.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(indexName = "stock")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

	@JSONField(name = "id") @Field("id")
    private Integer id;
	@JSONField(name = "name") @Field("name")
    private String name;
	@JSONField(name = "availability") @Field("availability")
    private Integer availability;
	@JSONField(name = "location") @Field("location")
    private String location;
}
