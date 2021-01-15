package ua.com.sipsoft.util.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for helper classes with fluent API_V1 for constructing sort order
 * lists. When the sort order is ready to be passed on, calling {@link #build()}
 * will create the list of sort orders.
 *
 * @param <T> the sort order type
 * @param <V> the sorting type
 *
 * @see AbstractSortOrderBuilder#thenAsc(Object)
 * @see AbstractSortOrderBuilder#thenDesc(Object)
 * @see #build()
 * @since 1.0
 */
public abstract class AbstractSortOrderBuilder<T extends SortOrder<V>, V>
		implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9198728005612558364L;

	/** The sort orders. */
	private final List<T> sortOrders = new ArrayList<>();

	/**
	 * Appends sorting with ascending sort direction.
	 *
	 * @param by the object to sort by
	 * @return this sort builder
	 */
	public AbstractSortOrderBuilder<T, V> thenAsc(V by) {
		return append(createSortOrder(by, SortDirection.asc));
	}

	/**
	 * Appends sorting with descending sort direction.
	 *
	 * @param by the object to sort by
	 * @return this sort builder
	 */
	public AbstractSortOrderBuilder<T, V> thenDesc(V by) {
		return append(createSortOrder(by, SortDirection.desc));
	}

	/**
	 * Returns an unmodifiable copy of the list of current sort orders in this sort
	 * builder.
	 *
	 * @return an unmodifiable sort order list
	 */
	public final List<T> build() {
		return Collections.unmodifiableList(new ArrayList<>(sortOrders));
	}

	/**
	 * Creates a sort order object with the given parameters.
	 *
	 * @param by        the object to sort by
	 * @param direction the sort direction
	 *
	 * @return the sort order object
	 */
	protected abstract T createSortOrder(V by, SortDirection direction);

	/**
	 * Append a sort order to {@code sortOrders}.
	 *
	 * @param sortOrder the sort order to append
	 * @return this
	 */
	private final AbstractSortOrderBuilder<T, V> append(T sortOrder) {
		sortOrders.add(sortOrder);
		return this;
	}
}
