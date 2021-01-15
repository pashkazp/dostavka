package ua.com.sipsoft.ui.controller.facility;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.util.AppURL;

@Controller
@RequestMapping(AppURL.FACILITIES)
@Slf4j
public class FacilitiesController {

	@GetMapping
	public String showAllFacilities() {
		// public String showAllFacilities(Model model, Locale locale) {
		Locale loc = LocaleContextHolder.getLocale();
		log.debug("Locale: {}", loc);
		return "facility/Facilities";
	}

}
