package ua.com.sipsoft.ui.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User Not Found") // 404
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8629778434033107371L;

	public UserNotFoundException(int id) {
		super("UserNotFoundException with id=" + id);
	}
}
