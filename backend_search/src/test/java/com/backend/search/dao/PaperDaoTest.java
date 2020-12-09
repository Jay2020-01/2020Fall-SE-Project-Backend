package com.backend.search.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.data.mongodb.core.query.TextQuery;

import com.backend.search.entity.Paper;

@SpringBootTest
class PaperDaoTest {

	@Autowired
	private PaperDao paperDao;
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Test
	void test1() {
		System.out.println("starting");
		Page<Paper> page = paperDao.findPaperByAuthorName("S. Cristiani", 0, 5);
		System.out.println(page.getContent());
		System.out.println(page.getSize());
		System.out.println("end");
	}
	
	@Test
	void test2() {
		System.out.println("starting");
		String id = "53e9a0e6b7602d97029d017b"; // pid
		String _id = "5fce0bd57952911c85691784"; //_id
		Paper p = mongoTemplate.findById(id, Paper.class);
		System.out.println(p);
		p = mongoTemplate.findById(_id, Paper.class);
		System.out.println(p);
	}

	@Test
	void test3() {
		// 在 title 字段上建索引
		TextIndexDefinition definition = new TextIndexDefinitionBuilder().onField("title").build();
		mongoTemplate.indexOps(Paper.class).ensureIndex(definition);
	}
	
	@Test
	void test4() {
		System.out.println("starting");
		Page<Paper> page = paperDao.findPaperByKeywords("cosmology", 0, 5);
		System.out.println(page.getContent());
		System.out.println(page.getTotalElements());
		System.out.println("end");
	}
	
	@Test
	void test5() {
		System.out.println("starting");
		Page<Paper> page = paperDao.findPaperByTitle("algorithm", 0, 5);
		System.out.println(page.getContent().size());
		System.out.println(page.getTotalElements());
		System.out.println("end");
	}
} 
