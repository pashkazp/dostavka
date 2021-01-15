package ua.com.sipsoft.util.query;

import java.io.Serializable;

/**
 * Sorting information for {@link Query}.
 *
 * @see Query
 * @since 1.0
 */
public class QuerySortOrder extends SortOrder<String> implements Serializable {

	private static final long serialVersionUID = 7658804991390916139L;

	/**
	 * Constructs sorting information for usage in a {@link Query}.
	 *
	 * @param sorted    sorting information, usually field id
	 * @param direction sorting direction
	 */
	public QuerySortOrder(String sorted, SortDirection direction) {
		super(sorted, direction);
	}

	/**
	 * Creates a new query sort builder with given sorting using ascending sort
	 * direction.
	 *
	 * @param by the string to sort by
	 *
	 * @return the query sort builder
	 */
	public static QuerySortOrderBuilder asc(String by) {
		return new QuerySortOrderBuilder().thenAsc(by);
	}

	/**
	 * Creates a new query sort builder with given sorting using descending sort
	 * direction.
	 *
	 * @param by the string to sort by
	 *
	 * @return the query sort builder
	 */
	public static QuerySortOrderBuilder desc(String by) {
		return new QuerySortOrderBuilder().thenDesc(by);
	}
}
