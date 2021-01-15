package ua.com.sipsoft.util.paging;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class that represents the sort order
 * 
 * @author Pavlo Degtyaryev
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Order implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5438776058894296061L;

	/** The column. */
	private Integer column;

	/** The dir. */
	private Direction dir;

}