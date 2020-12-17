package com.backend.search.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class author {
	private String name;
	private String org;
	@Field("id")
	private String id;
}
