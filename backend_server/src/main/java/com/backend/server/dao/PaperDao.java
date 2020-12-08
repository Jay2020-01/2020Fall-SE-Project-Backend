package com.backend.server.dao;

import com.backend.server.entity.Paper;

import java.util.List;

public interface PaperDao {

	Paper findPaperById(String id);

	List<Paper> Demo(String field, String context);
}
