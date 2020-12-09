package com.backend.server.dao;

import com.backend.server.entity.Author;

import java.util.List;

public interface AuthorDao {

	Author findAuthorById(String id);
	List<Author> Demo(String field, String context);
}
