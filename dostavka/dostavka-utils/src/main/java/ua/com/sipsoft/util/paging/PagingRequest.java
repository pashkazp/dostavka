package ua.com.sipsoft.util.paging;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class that represents pagin request criteria
 * 
 * @author Pavlo Degtyaryev
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagingRequest implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3583371664442568840L;

	/** The start page. */
	private int start;

	/** The length of list. */
	private int length;

	/** The draw. */
	private int draw;

	/** The sort order. */
	private List<Order> order;

	/** The list of columns. */
	private List<Column> columns;

	/** The search param. */
	private Search search;

}