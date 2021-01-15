package ua.com.sipsoft.service.util;

import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import ua.com.sipsoft.util.paging.Direction;
import ua.com.sipsoft.util.paging.PagingRequest;

/**
 * The Interface HasQueryToSortConvertor.
 */
public interface HasPagingRequestToSortConvertor {

	/**
	 * Query to sort.
	 *
	 * @param query the query
	 * @return the sort
	 */
	default Sort toSort(PagingRequest request) {
		return Sort.by(request.getOrder().stream()
				.map(order -> new Order(order.getDir() == Direction.asc ? Sort.Direction.ASC : Sort.Direction.DESC,
						request.getColumns().get(order.getColumn()).getData()))
				.collect(Collectors.toList()));
	}
}