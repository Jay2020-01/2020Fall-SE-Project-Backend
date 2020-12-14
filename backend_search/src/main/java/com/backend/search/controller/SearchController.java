package com.backend.search.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.search.service.SearchService;

@RestController
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
	public List<Map<String, Object>> search(String keyword, Integer pageNo, Integer pageSize) throws IOException {
		return searchService.searchPage(keyword.split(" "), pageNo, pageSize);
	}
}
