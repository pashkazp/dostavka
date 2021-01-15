package ua.com.sipsoft.ui.model.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoResponse {
	private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime timestamp;
	private String message;
	private String debugMessage;
	private List<AbstractSubInfoResponse> subInfos;

	public InfoResponse() {
		timestamp = LocalDateTime.now();
	}

	/**
	 * @param status
	 */
	public InfoResponse(HttpStatus status) {
		this();
		this.status = status;
	}

	public InfoResponse(HttpStatus status, Throwable ex) {
		this();
		this.status = status;
		this.message = "There is no detailed information";
		this.debugMessage = ex.getLocalizedMessage();
	}

	public InfoResponse(HttpStatus status, String message, Throwable ex) {
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	public InfoResponse(HttpStatus status, String message, String debugMessage) {
		this();
		this.status = status;
		this.message = message;
		this.debugMessage = debugMessage;
	}

	public void addSubInfo(AbstractSubInfoResponse info) {
		if (subInfos == null) {
			subInfos = new ArrayList<AbstractSubInfoResponse>();
		}
		subInfos.add(info);
	}
}
