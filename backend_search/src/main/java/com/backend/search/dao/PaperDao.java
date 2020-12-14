package com.backend.search.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import com.backend.search.entity.Paper;

public interface PaperDao {

	Paper findPaperById(String id);

	Paper findPaperBy_id(String _id);
	
	Paper findPaperBy_id(ObjectId _id);
	
	Page<Paper> findPaperByAuthorName(String name, Integer pageNum, Integer pageSize);

	Page<Paper> findPaperByAuthorId(String id, Integer pageNum, Integer pageSize);
	
//	Page<Paper> findPaperByTitle(String title, Integer pageNum, Integer pageSize);

//	Page<Paper> findPaperByPlainText(String context, Integer pageNum, Integer pageSize);

	Page<Paper> findPaperByKeywords(String input,Integer start_year,Integer end_year,Integer pageNum, Integer pageSize);
	
	List<Paper> Demo(String field, String context);
}
