package ua.com.sipsoft.ui.rest.v1.personal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.ui.model.request.SettingsBean;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.message.Response;

@Slf4j
@RestController
@RequestMapping(AppURL.USER_ENV)
public class SettingsRestController {

	@PostMapping
	public String procedSettings(@RequestBody SettingsBean settingsBean) {
		log.info("parameter is " + settingsBean.toString());
		return Response.SUCCESS;
	}
}
