package ua.com.sipsoft.ui.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.util.AppURL;

/**
 * The UsersController class handles the Users Page call
 */
@Controller
@RequestMapping(AppURL.USERS)
@Slf4j
public class UsersController {

	/**
	 * Return Users Page template
	 *
	 * @return the string
	 */
	// TODO юзер (если может) видит список всех подконтрольных ему пользователей и
	// выполняет доступный ему набор действий с ними
	@GetMapping
	public String showAllUsers() {
		return "users/Users";
	}

}
