package ua.com.sipsoft.ui.controller.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.util.AppURL;

@Controller
@RequestMapping(AppURL.ACCESS_DENIED_URL)
@Slf4j
public class error403Controller {

	@GetMapping
	public String showAllUsers() {
		return "error/403";
	}

}
