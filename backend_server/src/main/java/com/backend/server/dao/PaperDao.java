package com.backend.server.dao;

import java.util.List;
import com.backend.server.entity.Paper;

public interface PaperDao {

	Paper findPaperById(Integer id);

	List<Paper> Demo(String field, String context);
}
