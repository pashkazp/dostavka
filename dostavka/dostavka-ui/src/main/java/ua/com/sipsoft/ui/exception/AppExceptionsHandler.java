package ua.com.sipsoft.ui.exception;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import ua.com.sipsoft.service.exception.ResourceNotFoundException;
import ua.com.sipsoft.ui.model.response.InfoResponse;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.message.exception.ResourceNotFoundExceptionMsg;

@ControllerAdvice
public class AppExceptionsHandler {

	@Autowired
	private I18NProvider i18n;

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	public ResponseEntity<Object> handleUserServiceException(ResourceNotFoundException ex, WebRequest request,
			Locale loc) {
		String headers = request.getHeader(HttpHeaders.ACCEPT);

		MediaType mt;
		if (headers.indexOf(MediaType.APPLICATION_JSON_VALUE) == -1) {
			mt = MediaType.APPLICATION_XML;
		} else {
			mt = MediaType.APPLICATION_JSON;
		}
		InfoResponse infoResponse = new InfoResponse(HttpStatus.NOT_FOUND,
				i18n.getTranslation(ResourceNotFoundExceptionMsg.RESOURCE_NAME, loc) + ex.getResourceName(),
				i18n.getTranslation(ResourceNotFoundExceptionMsg.FIELD_NAME, loc) + ex.getFieldName() + " "
						+ ex.getFieldValue());
		return ResponseEntity.status(infoResponse.getStatus()).contentType(mt).body(infoResponse);
	}
}
