package ua.com.sipsoft.util.query;

import java.io.Serializable;

/**
 * Helper classes with fluent API_V1 for constructing {@link QuerySortOrder}
 * lists. When the sort order is ready to be passed on, calling {@link #build()}
 * will create the list of sort orders.
 *
 * @see QuerySortOrder
 * @see #thenDesc(String)
 * @see #thenDesc(String)
 * @see #build()
 * @since 1.0
 */
public class QuerySortOrderBuilder
		extends AbstractSortOrderBuilder<QuerySortOrder, String> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5154415042767455629L;

	/**
	 * Then asc.
	 *
	 * @param by the by
	 * @return the query sort order builder
	 */
	@Override
	public QuerySortOrderBuilder thenAsc(String by) {
		return (QuerySortOrderBuilder) super.thenAsc(by);
	}

	/**
	 * Then desc.
	 *
	 * @param by the by
	 * @return the query sort order builder
	 */
	@Override
	public QuerySortOrderBuilder thenDesc(String by) {
		return (QuerySortOrderBuilder) super.thenDesc(by);
	}

	/**
	 * Creates the sort order.
	 *
	 * @param by        the by
	 * @param direction the direction
	 * @return the query sort order
	 */
	@Override
	protected QuerySortOrder createSortOrder(String by,
			SortDirection direction) {
		return new QuerySortOrder(by, direction);
	}
}
