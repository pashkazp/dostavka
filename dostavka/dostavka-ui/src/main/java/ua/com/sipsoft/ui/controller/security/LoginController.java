package ua.com.sipsoft.ui.controller.security;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.sipsoft.util.message.LoginMsg;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(@RequestParam(value = "error") Optional<String> error,
			@RequestParam(value = "logout") Optional<String> logout, Model model) {
		if (error != null) {
			model.addAttribute(LoginMsg.LOGIN_ERROR_PARAMS, "Invalid email or password.");
			// result.reject(LoginMsg.LOGIN_ERROR_PARAMS, "Invalid username or password.");
		}

		if (logout != null) {
			model.addAttribute(LoginMsg.LOGOUT_SUCCESS, "You have been logged out.");
			// result.reject(LoginMsg.LOGOUT_SUCCESS, "You have been logged out.");
		}
		return "login/login";
	}
}
