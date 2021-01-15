package ua.com.sipsoft;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private final static Locale localeUK = new Locale("uk", "UA");
	private final static Locale localeRU = new Locale("ru", "RU");
	private final long MAX_AGE_SECS = 3600;

	@Bean
	public LocaleResolver localeResolver() {
//	SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
//	sessionLocaleResolver.setDefaultLocale(Locale.US);
//	return sessionLocaleResolver;
		CookieLocaleResolver r = new CookieLocaleResolver();
		r.setDefaultLocale(localeUK);
		r.setCookieName("localeInfo");
		r.setCookieMaxAge(30 * 24 * 60 * 60);

		return r;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(MAX_AGE_SECS);
	}

}
