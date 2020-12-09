package com.backend.search.dao;

import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Component;

import com.backend.search.entity.Paper;

@Component
public class PaperDaoImp implements PaperDao {

	private final int MAX_COUNT = 1000;
	
	@Resource
	private MongoTemplate mongoTemplate;

	@Override
	public Paper findPaperById(String id) {
		Query query = new Query(Criteria.where("pid").is(id));
		return mongoTemplate.findOne(query, Paper.class);
	}

	@Override
	public Paper findPaperBy_id(ObjectId _id) {
		Paper paper = mongoTemplate.findById(_id, Paper.class);
		return paper;
	}

	@Override
	public Paper findPaperBy_id(String _id) {
		Paper paper = mongoTemplate.findById(_id, Paper.class);
		return paper;
	}

	@Override
	public Page<Paper> findPaperByAuthorName(String name, Integer pageNum, Integer pageSize) {
		Query query = new Query();
		Pageable pageable = PageRequest.of(pageNum, pageSize);

		Pattern pattern = Pattern.compile(".*?" + name + ".*", Pattern.CASE_INSENSITIVE);
		query.addCriteria(Criteria.where("authors.name").regex(pattern));

		Long count;
		if (havePaper(query)) count = 1000L;
		else count = mongoTemplate.count(query, Paper.class);

		List<Paper> list = mongoTemplate.find(query.with(pageable), Paper.class);
		Page<Paper> page = new PageImpl<Paper>(list, pageable, count);

		return page;
	}

	@Override
	public Page<Paper> findPaperByAuthorId(String id, Integer pageNum, Integer pageSize) {
		if (id.equals("null")) {
			return null;
		}
		
		Query query = new Query();
		Pageable pageable = PageRequest.of(pageNum, pageSize);

		query.addCriteria(Criteria.where("authors.id").is(id));

		Long count;
		if (havePaper(query)) count = 1000L;
		else count = mongoTemplate.count(query, Paper.class);

		List<Paper> list = mongoTemplate.find(query.with(pageable), Paper.class);
		Page<Paper> page = new PageImpl<Paper>(list, pageable, count);

		return page;
	}
	
	@Override
	public Page<Paper> findPaperByTitle(String title, Integer pageNum, Integer pageSize) {
		TextQuery query = new TextQuery(title).sortByScore();
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		
		Long count;
		if (havePaper(query)) count = 1000L;
		else count = mongoTemplate.count(query, Paper.class);
		
		List<Paper> list = mongoTemplate.find(query.with(pageable), Paper.class);
		Page<Paper> page = new PageImpl<Paper>(list, pageable, count);
		return page;
	}
	
	/*
	@Override
	public Page<Paper> findPaperByPlainText(String context, Integer pageNum, Integer pageSize) {
		String[] text = context.split(" ");
		Query query = new Query();
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Criteria criteria = new Criteria();
		
		for (String str : text) {
			Pattern pattern = Pattern.compile(".*?" + str + ".*");
			query.addCriteria(Criteria.where("title").regex(pattern).and);
		}
		
		return null;
	}
	*/
	
	@Override
	public Page<Paper> findPaperByKeywords(String keywords, Integer pageNum, Integer pageSize) {
		String[] words = keywords.split(" ");
		Query query = new Query();
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Criteria criteria = new Criteria();
		
		for (String word : words) {
			criteria.and("keywords").is(word);
		}
		
		query = new Query(criteria);
		
		Long count;
		if (havePaper(query)) count = 1000L;
		else count = mongoTemplate.count(query, Paper.class);
		
		List<Paper> list = mongoTemplate.find(query.with(pageable), Paper.class);
		Page<Paper> page = new PageImpl<Paper>(list, pageable, count);
		
		return page;
	}
	
	@Override
	public List<Paper> Demo(String field, String context) {
		Pattern pattern = Pattern.compile(".*?" + context + ".*");
		Query query = new Query();
		query.addCriteria(Criteria.where(field).regex(pattern));
		return mongoTemplate.find(query, Paper.class);
	}
	
	private boolean havePaper(Query query) {
		query.skip(MAX_COUNT).limit(1);
		List<Paper> list = mongoTemplate.find(query, Paper.class);
		query.skip(0).limit(0);
		return list.size() >= 1;
	}
}