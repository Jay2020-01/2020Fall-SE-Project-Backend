package com.backend.search.controller;

import com.backend.search.entity.HotAuthor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.search.entity.Paper;
import com.backend.search.entity.pojo.Result;
import com.backend.search.entity.pojo.StatusCode;
import com.backend.search.service.SearchService;

import java.util.List;

@RestController
@RequestMapping("search")
public class SearchController {

	@Autowired
	SearchService searchService;

	/**
	 * 按 id (pid) 查询文章
	 *
	 * @param paperId
	 * @return
	 */
	@GetMapping("/id/{paperId}")
	public Result getPaperById(@PathVariable String paperId) {
		System.out.println("====== BEGIN FIND =====");
		Paper p = searchService.findPaperByPid(paperId);
		return Result.create(StatusCode.OK, "查询成功", p);
	}

	/**
	 *
	 * @return
	 */
	@GetMapping("/hotPaper")
	public Result getHotPaper() {
		List<Paper> p = searchService.findHotPaper();
		return Result.create(StatusCode.OK, "查询成功", p);
	}

	/**
	 *
	 * @return
	 */
	@GetMapping("/hotAuthorByC")
	public Result getHotAuthorByC() {
		List<HotAuthor> a = searchService.findHotAuthorByC();
		return Result.create(StatusCode.OK, "查询成功", a);
	}

	/**
	 *
	 * @return
	 */
	@GetMapping("/hotAuthorByH")
	public Result getHotAuthorByH() {
		List<HotAuthor> a = searchService.findHotAuthorByH();
		return Result.create(StatusCode.OK, "查询成功", a);
	}

	/**
	 *
	 * @param text
	 * @param startYear
	 * @param endYear
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/keyword/{text}/{startYear}/{endYear}/{pageNum}/{pageSize}")
	public Result findPaperBykeyword(@PathVariable String text, @PathVariable Integer startYear,
			@PathVariable Integer endYear, @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
		Page<Paper> page = searchService.findPaperByKeywords(text, startYear, endYear, pageNum, pageSize);
		return Result.create(StatusCode.OK, "查询成功", page);
	}
}
