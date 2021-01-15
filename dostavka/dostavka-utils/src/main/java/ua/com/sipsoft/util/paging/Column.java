package ua.com.sipsoft.util.paging;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * class that represents the column of table
 * 
 * @author Pavlo Degtyaryev
 *
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Column implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The data. */
	private String data;

	/** The name. */
	private String name;

	/** The searchable. */
	private Boolean searchable;

	/** The orderable. */
	private Boolean orderable;

	/** The search. */
	private Search search;
}