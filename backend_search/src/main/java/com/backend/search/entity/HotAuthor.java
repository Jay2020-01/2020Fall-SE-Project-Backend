package com.backend.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
//@Document(collection = "c_h_author")
public class HotAuthor {

	@Id
	private String _id;

	private String aid;
	private String name;
	private Integer h_index;
	private Integer n_pubs;
	private Integer n_citation;
	private Integer userId;
}
