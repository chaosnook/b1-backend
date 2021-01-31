package com.game.b1ingservice.specification.commons;

import com.game.b1ingservice.commons.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;



/**
 * @param <B> Object type of SearchPageable
 * @param <T> Domain object
 */
public abstract class SearchPageSpecification<B extends SearchPageable, T> extends SearchBodySpecification<B,T> {

	private static final long serialVersionUID = 1L;

	public SearchPageSpecification(B searchBody) {
		super(searchBody);
	}
	
	/**
	 * OVerride this method. if you want to customize the sort property.
	 */
	protected String sortProperty(String sortField) {
		return sortField;
	}
	
	/**
	 * OVerride this method. if you want to customize the sort directions.
	 */
	protected Sort buildSort(String sort, String sortField) {
		switch (StringUtils.trimToEmpty(sort)) {
			case Constants.Sort.DESC : return Sort.by(Sort.Order.desc(sortField));
			default   : return Sort.by(Sort.Order.asc(sortField));
		}
	}
	
	public Pageable getPageable() {
		Integer page = this.searchBody.getPage();
		Integer size = this.searchBody.getSize();
		String sort = this.searchBody.getSort();
		String sortField = sortProperty(this.searchBody.getSortField());
		return PageRequest.of(page, size, buildSort(sort, sortField));
	}

}
