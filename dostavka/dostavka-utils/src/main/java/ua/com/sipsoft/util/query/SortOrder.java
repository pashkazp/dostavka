package ua.com.sipsoft.util.query;

import java.io.Serializable;

/**
 * Sorting information for one field.
 *
 * @param <T> the type of the sorting information, usually a String (field id)
 *            or a {@link java.util.Comparator}.
 * @since 1.0
 */
public class SortOrder<T> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7092306587430265010L;

	/** The sorted item. */
	private final T sorted;

	/** The direction. */
	private final SortDirection direction;

	/**
	 * Constructs a field sorting information.
	 *
	 * @param sorted    sorting information, usually field id or
	 *                  {@link java.util.Comparator}
	 * @param direction sorting direction
	 */
	public SortOrder(T sorted, SortDirection direction) {
		this.sorted = sorted;
		this.direction = direction;
	}

	/**
	 * Sorting information.
	 *
	 * @return sorting entity, usually field id or {@link java.util.Comparator}
	 */
	public T getSorted() {
		return sorted;
	}

	/**
	 * Sorting direction.
	 *
	 * @return sorting direction
	 */
	public SortDirection getDirection() {
		return direction;
	}
}
