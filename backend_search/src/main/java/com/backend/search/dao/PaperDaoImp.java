package com.backend.search.dao;

import com.backend.search.entity.HotAuthor;
import com.backend.search.entity.Paper;
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

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
	public Map findPaperById_(String id) {
		Query query = new Query(Criteria.where("pid").is(id));
		return mongoTemplate.findOne(query, Map.class, "paper");
	}

	@Override
	public Paper findPaperBy_id(ObjectId _id) {
		return mongoTemplate.findById(_id, Paper.class);
	}

	@Override
	public Paper findPaperBy_id(String _id) {
		return mongoTemplate.findById(_id, Paper.class);
	}

	@Override
	public Page<Paper> findPaperByAuthorName(String name, Integer pageNum, Integer pageSize) {
		Query query = new Query();
		Pageable pageable = PageRequest.of(pageNum, pageSize);

		query.addCriteria(Criteria.where("authors.name").is(name));
		Long count;
		if (havePaper(query))
			count = 1000L;
		else
			count = mongoTemplate.count(query, Paper.class);

		List<Paper> list = mongoTemplate.find(query.with(pageable), Paper.class);

		return new PageImpl<Paper>(list, pageable, count);
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
		if (havePaper(query))
			count = 1000L;
		else
			count = mongoTemplate.count(query, Paper.class);

		List<Paper> list = mongoTemplate.find(query.with(pageable), Paper.class);

		return new PageImpl<Paper>(list, pageable, count);
	}

	@Override
	public Page<Paper> findPaperByTitle(String title, Integer pageNum, Integer pageSize) {
		TextQuery query = new TextQuery(title);
		Pageable pageable = PageRequest.of(pageNum, pageSize);

		Long count;
		if (havePaper(query))
			count = 1000L;
		else
			count = mongoTemplate.count(query, Paper.class);

		List<Paper> list = mongoTemplate.find(query.with(pageable), Paper.class);
		return new PageImpl<Paper>(list, pageable, count);
	}

	@Override
	public Page<Paper> findPaperByKeywords(String input, Integer startYear, Integer endYear, Integer pageNum,
			Integer pageSize) {

		TextQuery query = new TextQuery(input);
		if (startYear != null && startYear <= 1901)
			startYear = null;
		if (endYear != null && endYear >= 2025)
			endYear = null;

		if (startYear != null && endYear != null)
			query.addCriteria(Criteria.where("year").gte(startYear).lte(endYear));
		else if (endYear != null)
			query.addCriteria(Criteria.where("year").lte(endYear));
		else if (startYear != null)
			query.addCriteria(Criteria.where("year").gte(startYear));

		Pageable pageable = PageRequest.of(pageNum, pageSize);

		Long count;
		if (havePaper(query))
			count = 1000L;
		else
			count = mongoTemplate.count(query, Paper.class);

		List<Paper> list = mongoTemplate.find(query.with(pageable), Paper.class);
		return new PageImpl<Paper>(list, pageable, count);
	}

	@Override
	public List<Paper> Demo(String field, String context) {
		Pattern pattern = Pattern.compile(".*?" + context + ".*");
		Query query = new Query();
		query.addCriteria(Criteria.where(field).regex(pattern));
		return mongoTemplate.find(query, Paper.class);
	}

	@Override
	public List<Paper> findHotPaper() {
		Query query = new Query();
		query.fields().include("title");
		query.fields().include("pid");
		query.fields().include("n_citation");
		return mongoTemplate.find(query, Paper.class, "c_h_paper");
	}

	@Override
	public List<HotAuthor> findHotAuthorByH() {
		Query query = new Query();
		query.fields().include("aid");
		query.fields().include("name");
		query.fields().include("h_index");
		return mongoTemplate.find(query, HotAuthor.class, "h_h_author");
	}

	@Override
	public List<HotAuthor> findHotAuthorByC() {
		Query query = new Query();
		query.fields().include("aid");
		query.fields().include("name");
		query.fields().include("n_citation");
		return mongoTemplate.find(query, HotAuthor.class, "c_h_author");
	}

	private boolean havePaper(Query query) {
		query.skip(MAX_COUNT).limit(1);
		List<Paper> list = mongoTemplate.find(query, Paper.class);
		query.skip(0).limit(0);
		return list.size() >= 1;
	}
}
