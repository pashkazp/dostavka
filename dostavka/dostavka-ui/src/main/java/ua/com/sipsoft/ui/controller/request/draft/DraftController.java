package ua.com.sipsoft.ui.controller.request.draft;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.util.AppURL;

@Controller
@RequestMapping(AppURL.DRAFT)
@Slf4j
public class DraftController {

	@GetMapping
	public String showAllFacilities() {
		Locale loc = LocaleContextHolder.getLocale();
		log.debug("Locale: {}", loc);
		return "requests/draft/Drafts";
	}

}
