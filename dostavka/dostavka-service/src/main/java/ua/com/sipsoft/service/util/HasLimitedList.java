package ua.com.sipsoft.service.util;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Interface HasLimitedList.
 */
public interface HasLimitedList {

	/**
	 * Query to sort.
	 * 
	 * @param <T>
	 *
	 * @param query the query
	 * @return the sort
	 */
	default <T> List<T> getLimitedList(List<T> entitys, long skip, long limit) {
		if (limit < 0) {
			return entitys;
		}
		return entitys.stream()
				.skip(skip)
				.limit(limit)
				.collect(Collectors.toList());
	}
}