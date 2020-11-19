package com.backend.search.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
	
	@Autowired
	@Qualifier("restHighLevelClient")
	private RestHighLevelClient client;
	
	/**
	 * 单个词完全匹配;多个词模糊匹配
	 * @param keyword
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, Object>> searchPage(String[] keyword, int pageNo, int pageSize) throws IOException {
		if (pageNo < 1) pageNo = 1;
		
		SearchRequest searchRequest = new SearchRequest("Paper");
		SearchSourceBuilder builder = new SearchSourceBuilder();
		
		// 查询条件
		builder.from((pageNo - 1) * pageSize);
		builder.size(pageSize);
		if (keyword.length <= 1)
			builder.query(QueryBuilders.termQuery("title", keyword));
		else 
			builder.query(QueryBuilders.termsQuery("title", keyword));
		builder.timeout(new TimeValue(5, TimeUnit.SECONDS));

		//高亮
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("title");
		highlightBuilder.requireFieldMatch(false);
		highlightBuilder.preTags("<span style='color:red'>");
		highlightBuilder.postTags("</span>");
		builder.highlighter(highlightBuilder);
		
		searchRequest.source(builder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        
        // 取出数据
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit documentFields : searchResponse.getHits().getHits()) {

        	Map<String, HighlightField> highlightFieldMap = documentFields.getHighlightFields();
        	HighlightField title = highlightFieldMap.get("title");
        	Map<String,Object> sourceAsMap = documentFields.getSourceAsMap();
        	//解析高亮字段
			if(title!=null){
				Text[] fragments = title.fragments();
				String new_title = "";
				for(Text text : fragments){
					new_title += text;
				}
				sourceAsMap.put("title",new_title);
			}
            list.add(sourceAsMap);
        }
        
        return list;
		
	}
	
	/**
	 * 尝试的条件查询，指定查询的发表时间的范围。同时模糊搜索标题，完全匹配作者；
	 * 可以根据需求进行修改
	 * @param begin
	 * @param end
	 * @param title
	 * @param author
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, Object>> publishTimeFilterSearch (Date begin, Date end, String title, String author, Integer pageNo, Integer pageSize) throws IOException {
		SearchRequest searchRequest = new SearchRequest("Paper");
		SearchSourceBuilder builder = new SearchSourceBuilder();
		
		builder.from((pageNo - 1) * pageSize);
		builder.size(pageSize);
				
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		if (title != null) boolQuery.filter(QueryBuilders.termsQuery("title", title));
		if (author != null) boolQuery.filter(QueryBuilders.termQuery("authorName", author));
		if (begin != null) boolQuery.filter(QueryBuilders.rangeQuery("publishTime").gte(begin));
		if (end != null) boolQuery.filter(QueryBuilders.rangeQuery("publishTime").lte(end));
		
		builder.query(boolQuery);
		searchRequest.source(builder);
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		
		ArrayList<Map<String, Object>> list = new ArrayList<>();
		for (SearchHit documentFields : searchResponse.getHits().getHits()) {
			list.add(documentFields.getSourceAsMap());
		}
		
		return list;
	}
}
