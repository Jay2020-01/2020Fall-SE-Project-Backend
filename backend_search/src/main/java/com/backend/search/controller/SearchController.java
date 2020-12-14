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

	@GetMapping("/ping")
	public Result Ping() {
		System.out.println("====== Ping Success =====");
		return Result.create(StatusCode.OK, "查询成功", "SUCCESS");
	}

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

	@GetMapping("/index/hotPaper")
	public Result getHotPaper() {
		List<Paper> p = searchService.findHotPaper();
		return Result.create(StatusCode.OK, "查询成功", p);
	}

	@GetMapping("/index/hotAuthorByC")
	public Result getHotAuthorByC() {
		List<HotAuthor> a = searchService.findHotAuthorByC();
		return Result.create(StatusCode.OK, "查询成功", a);
	}

	@GetMapping("/index/hotAuthorByH")
	public Result getHotAuthorByH() {
		List<HotAuthor> a = searchService.findHotAuthorByH();
		return Result.create(StatusCode.OK, "查询成功", a);
	}
//	@GetMapping("/title/{text}/{pageNum}/{pageSize}")
//	public Result findPaperByTitle(@PathVariable String text,@PathVariable Integer pageNum,@PathVariable Integer pageSize) {
//		System.out.println("===== BEGIN SEARCH =====");
//		Page<Paper> page = searchService.findPaperByTitle(text, pageNum, pageSize);
//		if (page != null)
//			return Result.create(StatusCode.OK, "查询成功", page);
//		else
//			return Result.create(StatusCode.NOTFOUND, "文章不存在");
//	}

	/**
	 * 按 keyword 查询
	 *
	 * @param text
	 * @param start_year
	 * @param end_year
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/keyword/{text}/{start_year}/{end_year}/{pageNum}/{pageSize}")
	public Result findPaperBykeyword(@PathVariable String text,@PathVariable Integer start_year,@PathVariable Integer end_year,@PathVariable Integer pageNum,@PathVariable Integer pageSize) {
		Page<Paper> page = searchService.findPaperByKeywords(text,start_year,end_year, pageNum, pageSize);
		return Result.create(StatusCode.OK, "查询成功", page);
	}
}
