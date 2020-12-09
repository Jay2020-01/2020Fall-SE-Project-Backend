package com.backend.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.search.entity.Paper;
import com.backend.search.entity.pojo.Result;
import com.backend.search.entity.pojo.StatusCode;
import com.backend.search.service.PaperService;

@RestController
@RequestMapping("paper")
public class PaperController {

	@Autowired
	PaperService paperService;

	@GetMapping("/id/{paperId}")
	/**
	 * 按 id (pid) 查询文章
	 * 
	 * @param paperId
	 * @return
	 */
	public Result getPaperById(@PathVariable String paperId) {
		Paper p = paperService.findPaperByPid(paperId);
		return Result.create(StatusCode.OK, "查询成功", p);
	}

	/**
	 * 按标题查询，默认按匹配程度排序
	 * 效率不错
	 * 
	 * @param text     查询内容
	 * @param pageNum  页码，从 0 开始
	 * @param pageSize 每页个数，必须 > 0，建议不超过 30
	 * @return
	 */
	@GetMapping("/title/{text}/{pageNum}/{pageSize}")
	public Result findPaperByTitle(@PathVariable String text,@PathVariable Integer pageNum,@PathVariable Integer pageSize) {
		Page<Paper> page = paperService.findPaperByTitle(text, pageNum, pageSize);
		if (page != null)
			return Result.create(StatusCode.OK, "查询成功", page);
		else 
			return Result.create(StatusCode.NOTFOUND, "文章不存在");
	}

	/**
	 * 按 keyword 查询，目前最好按单个单词查，比如 cosmology 这种
	 * 然后他现在有点慢，白天研究一下
	 * 
	 * @param text
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@GetMapping("keyword/{text}/{pageNum}/{pageSize}")
	public Result findPaperBykeyword(@PathVariable String text,@PathVariable Integer pageNum,@PathVariable Integer pageSize) {
		Page<Paper> page = paperService.findPaperByKeywords(text, pageNum, pageSize);
		return Result.create(StatusCode.OK, "查询成功", page);
	}
}
