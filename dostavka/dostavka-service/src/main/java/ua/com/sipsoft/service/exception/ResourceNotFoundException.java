package ua.com.sipsoft.service.exception;

import lombok.Getter;

/**
 * The Class ResourceNotFoundException used throughout the application for
 * Resource Not Found exceprion
 * 
 * @author Pavlo Degtyaryev
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4501691224500979786L;

	/** The resource name. */
	private String resourceName;

	/** The field name. */
	private String fieldName;

	/** The field value. */
	private Object fieldValue;

	/**
	 * Instantiates a new resource not found exception.
	 *
	 * @param String the resource name
	 * @param String the field name
	 * @param Object the field value
	 */
	public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
		super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

}
