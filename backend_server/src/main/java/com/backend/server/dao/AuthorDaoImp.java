package com.backend.server.dao;

import com.backend.server.entity.Author;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class AuthorDaoImp implements AuthorDao {

	@Resource
	private MongoTemplate mongoTemplate;


	@Override
	public Author findAuthorById(String id) {
		Query query = new Query(Criteria.where("aid").is(id));
		return mongoTemplate.findOne(query, Author.class);
	}
	@Override
	public List<Author> Demo(String field, String context) {
		Pattern pattern = Pattern.compile(".*?" + context + ".*");
		Query query = new Query();
		query.addCriteria(Criteria.where(field).regex(pattern));
		return mongoTemplate.find(query, Author.class);
	}

}
