package com.backend.search.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.backend.search.dao.PaperDao;
import com.backend.search.entity.Paper;

@Service
public class SearchService {

	@Autowired
	private PaperDao paperDao;

	public Paper findPaperByPid(String pid) {
		return paperDao.findPaperById(pid);
	}

	public Paper findPaperBy_id(String _id) {
		return paperDao.findPaperBy_id(_id);
	}

	public Paper findPaperBy_id(ObjectId _id) {
		return paperDao.findPaperBy_id(_id);
	}

	public Page<Paper> findPaperByAuthorName(String name, Integer pageNum, Integer pageSize) {
		return paperDao.findPaperByAuthorName(name, pageNum, pageSize);
	}

	public Page<Paper> findPaperByAuthorId(String id, Integer pageNum, Integer pageSize) {
		return paperDao.findPaperByAuthorId(id, pageNum, pageSize);
	}

	public Page<Paper> findPaperByTitle(String title, Integer pageNum, Integer pageSize) {
		return paperDao.findPaperByTitle(title, pageNum, pageSize);
	}

	public Page<Paper> findPaperByKeywords(String input,Integer start_year,Integer end_year,Integer pageNum, Integer pageSize) {
		return paperDao.findPaperByKeywords(input, start_year, end_year, pageNum, pageSize);
	}
}
