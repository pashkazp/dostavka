package ua.com.sipsoft.util.query;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import ua.com.sipsoft.util.paging.PagingRequest;

/**
 * Immutable query object used to request data from a backend. Contains index
 * limits, sorting and filtering information.
 *
 * @param <T> bean type
 * @param <F> filter type
 * @since 1.0
 */
public class Query<T, F> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3433622748288654805L;

	/** The offset. */
	private final int offset;

	/** The limit. */
	private final int limit;

	/** The sort orders. */
	private final List<QuerySortOrder> sortOrders;

	/** The in memory sorting. */
	private final Comparator<T> inMemorySorting;

	/** The filter. */
	private final F filter;

	/**
	 * Constructs a Query for all rows from 0 to {@link Integer#MAX_VALUE} without
	 * sorting and filtering.
	 */
	public Query() {
		offset = 0;
		limit = Integer.MAX_VALUE;
		sortOrders = Collections.emptyList();
		inMemorySorting = null;
		filter = null;
	}

	/**
	 * Constructs a Query for all rows from 0 to {@link Integer#MAX_VALUE} with
	 * filtering.
	 *
	 * @param filter back end filter of a suitable type for the data provider; can
	 *               be null
	 */
	public Query(F filter) {
		offset = 0;
		limit = Integer.MAX_VALUE;
		sortOrders = Collections.emptyList();
		inMemorySorting = null;
		this.filter = filter;
	}

	/**
	 * Constructs a new Query object with given offset, limit, sorting and
	 * filtering.
	 *
	 * @param offset          first index to fetch
	 * @param limit           fetched item count
	 * @param sortOrders      sorting order for fetching; used for sorting backends
	 * @param inMemorySorting comparator for sorting in-memory data
	 * @param filter          filtering for fetching; can be null
	 */
	public Query(int offset, int limit, List<QuerySortOrder> sortOrders,
			Comparator<T> inMemorySorting, F filter) {
		this.offset = offset;
		this.limit = limit;
		this.sortOrders = sortOrders;
		this.inMemorySorting = inMemorySorting;
		this.filter = filter;
	}

	/**
	 * Instantiates a new query.
	 *
	 * @param pagingRequest the paging request
	 * @param filter        the filter
	 */
	public Query(PagingRequest pagingRequest, F filter) {
		this.inMemorySorting = null;
		this.offset = pagingRequest.getStart();
		this.limit = pagingRequest.getLength();
		this.filter = filter;
		this.sortOrders = new LinkedList<>();
//	for (Order order : pagingRequest.getOrder()) {
//	    QuerySortOrder so = new QuerySortOrder(pagingRequest.getColumns().get(order.getColumn()).getData(),
//		    order.getDir());
//	    sortOrders.add(so);
//	}

	}

	/**
	 * Gets the first index of items to fetch. The offset is only used when fetching
	 * items, but not when counting the number of available items.
	 *
	 * @return offset for data request
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Gets the number of items to fetch. The limit is only used when fetching
	 * items, but not when counting the number of available items.
	 * <p>
	 * <strong>Note: </strong>It is possible that
	 * {@code offset + limit > item count}
	 *
	 * @return number of items to fetch
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * Gets the sorting for items to fetch. This list of sort orders is used for
	 * sorting backends. The sort orders are only used when fetching items, but not
	 * when counting the number of available items.
	 * <p>
	 * <strong>Note: </strong> Sort orders and in-memory sorting are mutually
	 * exclusive. If the {@link DataProvider} handles one, it should ignore the
	 * other.
	 *
	 * @return list of sort orders
	 */
	public List<QuerySortOrder> getSortOrders() {
		return sortOrders;
	}

	/**
	 * Gets the filter for items to fetch.
	 *
	 * @return optional filter
	 */
	public Optional<F> getFilter() {
		return Optional.ofNullable(filter);
	}

	/**
	 * Gets the comparator for sorting in-memory data. The comparator is only used
	 * when fetching items, but not when counting the number of available items.
	 * <p>
	 * <strong>Note: </strong> Sort orders and in-memory sorting are mutually
	 * exclusive. If the {@link DataProvider} handles one, it should ignore the
	 * other.
	 *
	 * @return sorting comparator
	 */
	public Comparator<T> getInMemorySorting() {
		return inMemorySorting;
	}

	/**
	 * Gets the optional comparator for sorting data. The comparator is only used
	 * when fetching items, but not when counting the number of available items.
	 * <p>
	 * <strong>Note: </strong> Sort orders and comparator sorting are mutually
	 * exclusive. If the {@link DataProvider} handles one, it should ignore the
	 * other.
	 *
	 * @return optional sorting comparator
	 */
	public Optional<Comparator<T>> getSortingComparator() {
		return Optional.ofNullable(inMemorySorting);
	}

}
