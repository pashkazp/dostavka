package ua.com.sipsoft.ui.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationInfoResponse extends AbstractSubInfoResponse {
	private String field;
	private String message;

	public ValidationInfoResponse(String field, String message) {
		this.field = field;
		this.message = message;
	}
}
