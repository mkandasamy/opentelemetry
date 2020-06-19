package com.lfg.poc.opentelemetry.dao.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Document(indexName = "review")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

	@JSONField(name = "id") @Field("id")
    private Integer id;
	@JSONField(name = "rating") @Field("rating")
    private Integer rating;
	@JSONField(name = "reviews") @Field("reviews")
    private Integer reviews;
}
