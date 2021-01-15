package ua.com.sipsoft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.security.impl.CustomOAuth2UserServiceImpl;
import ua.com.sipsoft.service.security.impl.CustomOpenIdUserServiceImpl;
import ua.com.sipsoft.service.user.UserService;
import ua.com.sipsoft.ui.security.CustomAccessDeniedHandler;
import ua.com.sipsoft.ui.security.CustomAuthenticationSuccessHandler;
import ua.com.sipsoft.ui.security.JwtAuthenticationFilter;
import ua.com.sipsoft.util.AppProperties;
import ua.com.sipsoft.util.AppURL;

/**
 * The Class SecurityConfig.
 *
 * @author Pavlo Degtyaryev
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Authentication provider.
	 *
	 * @return the authentication provider
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		log.info("Create AuthenticationProvider");
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// Setting Service to find User in the database.
		// And Setting PassswordEncoder
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
	}

	@Order(1)
	@Configuration
	public static class RestConfiguration extends WebSecurityConfigurerAdapter {
		@Autowired
		private JwtAuthenticationFilter jwtAuthenticationFilter;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.antMatcher(AppURL.API_V1 + "/**")
					.cors()
					.and()
					.csrf()
					.disable() // we don't need CSRF because our token is invulnerable
					.authorizeRequests()
					.antMatchers(HttpMethod.POST, AppURL.API_V1_AUTH + "/**").permitAll()
					// .antMatchers(RESET_PASSWORD_URL).permitAll()
					.anyRequest().authenticated()
					.and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

			http.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		}

	}

	@Order(2)
	@Configuration
	public static class WebConfiguration extends WebSecurityConfigurerAdapter {
		@Autowired
		private JwtAuthenticationFilter jwtAuthenticationFilter;

		@Autowired
		private CustomAccessDeniedHandler customAccessDeniedHandler;

		@Autowired // do not put to constructor. unresolvable circular reference
		private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

		@Autowired
		private CustomOAuth2UserServiceImpl customOAuth2UserService;

		@Autowired
		private CustomOpenIdUserServiceImpl customOpenIdUserService;

		@Autowired
		private AppProperties appProperties;

		@Autowired
		private UserService userService;

		@Autowired
		PasswordEncoder passwordEncoder;

		@Bean(BeanIds.AUTHENTICATION_MANAGER)
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Override
		public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
			authenticationManagerBuilder
					.userDetailsService(userService)
					.passwordEncoder(passwordEncoder);
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web
					.ignoring()
					.antMatchers("/resources/**")
					.antMatchers("/publics/**")
					.antMatchers("/resources/**")
					// the standard favicon URI
					.antMatchers("/favicon.ico")

					// "/images/.*",
					.antMatchers("/registration/**")
					.antMatchers("/js/**")
					.antMatchers("/css/**")
					.antMatchers("/img/**")
					.antMatchers("/webjars/**")
					.antMatchers("/assets/**")
					// the robots exclusion standard
					.antMatchers("/robots.txt")
					// web application manifest
					.antMatchers("/manifest.webmanifest")
					.antMatchers("/sw.js")
					.antMatchers("/offline-page.html");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			log.info("Configure HttpSecurity");
			// @formatter:off

			http
					.cors()
					.and()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
					.csrf().disable() // CSRF handled
					.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
					.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(AppURL.LOGIN))

					.and()
					.authorizeRequests()
					.antMatchers(AppURL.OAUTH2 + "/**")
					.permitAll()
					.antMatchers(AppURL.LOGIN + "/**",
							AppURL.LOGIN_FORGOT + "/**",
							AppURL.LOGIN_REGISTRATION + "/**",
							"/" + AppURL.REGISTRATION_CONFIRM + "/**")
					.permitAll()
					.antMatchers("/" + AppURL.LOGOUT_URL + "/**")
					.permitAll()
					.antMatchers(AppURL.ACCESS_DENIED_URL + "/**",
							AppURL.ERROR + "/**")
					.permitAll()

					.regexMatchers(HttpMethod.POST, "/\\?v-r=.*").permitAll()
					.antMatchers("/**")
					.fullyAuthenticated()

					.and()
					.formLogin()
					.loginPage(AppURL.LOGIN)
					.usernameParameter("email").passwordParameter("password")
					.successHandler(customAuthenticationSuccessHandler)

					.and()
					.oauth2Login()
					.authorizationEndpoint()
					.baseUri(AppURL.OAUTH2_AUTORIZE)
					.and()
					.redirectionEndpoint()
					.baseUri(AppURL.OAUTH2_CALLBACK + "/*")
					.and()
					.userInfoEndpoint()
					.userService(customOAuth2UserService)
					.oidcUserService(customOpenIdUserService)
					.and()
					.successHandler(customAuthenticationSuccessHandler)
					// TODO exception on wrong password

					.and()
					.rememberMe()// remember me,
					.tokenValiditySeconds(86400)// remember me for one day
					.key("logisticSecret!")// hash key
					.rememberMeCookieName("remember_me")
					.rememberMeParameter("checkRememberMe")// remember me field name in login form

					.and()
					.logout()
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.deleteCookies(appProperties.getAuth().getTokenCookieName(), "remember_me")
					.logoutRequestMatcher(new AntPathRequestMatcher("/" + AppURL.LOGOUT_URL))
					.logoutSuccessUrl(AppURL.LOGIN + "?logout")
					.permitAll();

			// Add our custom Token based authentication filter
			http.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		}
	}

}
