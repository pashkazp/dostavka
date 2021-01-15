package ua.com.sipsoft.util.query;

import java.io.Serializable;

/**
 * Describes sorting direction.
 *
 * @author Ltd
 * @since 1.0
 */
public enum SortDirection implements Serializable {

	/**
	 * Ascending (e.g. A-Z, 1..9) sort order
	 */
	asc {
		@Override
		public SortDirection getOpposite() {
			return desc;
		}
	},

	/**
	 * Descending (e.g. Z-A, 9..1) sort order
	 */
	desc {
		@Override
		public SortDirection getOpposite() {
			return asc;
		}
	};

	/**
	 * Get the sort direction that is the direct opposite to this one.
	 *
	 * @return a sort direction value
	 */
	public abstract SortDirection getOpposite();

}
