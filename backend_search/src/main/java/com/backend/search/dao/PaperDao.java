package com.backend.search.dao;

import com.backend.search.entity.HotAuthor;
import com.backend.search.entity.Paper;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface PaperDao {

	Paper findPaperById(String id);

	Map findPaperById_(String id);
	
	Paper findPaperBy_id(String _id);

	Paper findPaperBy_id(ObjectId _id);
	
	Page<Paper> findPaperByAuthorName(String name, Integer pageNum, Integer pageSize);

	Page<Paper> findPaperByAuthorId(String id, Integer pageNum, Integer pageSize);

	Page<Paper> findPaperByTitle(String title, Integer pageNum, Integer pageSize);

	Page<Paper> findPaperByKeywords(String input, Integer startYear, Integer endYear, Integer pageNum,
			Integer pageSize);

	List<Paper> Demo(String field, String context);

	List<Paper> findHotPaper();

	List<HotAuthor> findHotAuthorByH();

	List<HotAuthor> findHotAuthorByC();
}
