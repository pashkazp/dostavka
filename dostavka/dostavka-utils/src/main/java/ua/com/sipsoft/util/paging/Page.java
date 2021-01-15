package ua.com.sipsoft.util.paging;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class that represents page of data
 * 
 * @author Pavlo Degtyaryev
 *
 * @param <T> classified the type of data on page
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Page<T> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -130623465943254595L;

	/**
	 * Instantiates a new page.
	 *
	 * @param data the data
	 */
	public Page(List<T> data) {
		this.data = data;
	}

	public Page(Page<?> page, List<T> data) {
		this.data = data;
		this.draw = page.draw;
		this.recordsFiltered = page.recordsFiltered;
		this.recordsTotal = page.recordsTotal;
	}

	/** The data. */
	private List<T> data;

	/** The records filtered. */
	private int recordsFiltered;

	/** The records total. */
	private int recordsTotal;

	/** The draw. */
	private int draw;

}