package ua.com.sipsoft.service.util;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Interface HasQueryToSortConvertor.
 */
public interface HasFilteredList {

	/**
	 * Query to sort.
	 * 
	 * @param <T>
	 *
	 * @param query the query
	 * @return the sort
	 */
	default <T> List<T> getFiteredList(List<T> entitys, EntityFilter<T> entityFilter) {
		if (entityFilter == null) {
			return entitys;
		}
		return entitys.stream()
				.filter(entity -> entityFilter.isPass(entity))
				.collect(Collectors.toList());
	}
}