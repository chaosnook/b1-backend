package com.game.b1ingservice.specification.commons;

import lombok.Data;


@Data
public class SearchPageable {
	
	 private Integer page = 0;
	 private Integer size = 10;
	 private String sort;
	 private String sortField = "id";

}
