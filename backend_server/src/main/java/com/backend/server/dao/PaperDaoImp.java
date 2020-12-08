package com.backend.server.dao;

import com.backend.server.entity.Paper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PaperDaoImp implements PaperDao {

	@Resource
	private MongoTemplate mongoTemplate;


	@Override
	public Paper findPaperById(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, Paper.class);
	}

	@Override
	public List<Paper> Demo(String field, String context) {
		Pattern pattern = Pattern.compile(".*?" + context + ".*");
		Query query = new Query();
		query.addCriteria(Criteria.where(field).regex(pattern));
		return mongoTemplate.find(query, Paper.class);
	}


}
