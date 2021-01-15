package ua.com.sipsoft.service.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.sipsoft.util.security.Role;
import ua.com.sipsoft.web.DostavkaApplicationTest;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration

class UsersUtilsTest extends DostavkaApplicationTest {

	@Autowired
	AuthenticationManager authenticationManager;

	@BeforeEach
	void clearSecurityContextHolder() {
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("Test UserUtils without any logged User")
	void test1() {
		String name = UsersUtils.getUsername();
		assertNull(name);
	}

	@Test
	@DisplayName("Test UserUtils with logged User")
	void test2() {
		UsernamePasswordAuthenticationToken s = new UsernamePasswordAuthenticationToken(
				"admin@sipsoft.com.ua",
				"Admin");
		Authentication authentication = authenticationManager.authenticate(s);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		assertEquals("admin@sipsoft.com.ua", UsersUtils.getUsername());
	}

	@Test
	@DisplayName("Test UserUtils with logged and unlogged User")
	void test3() {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						"admin@sipsoft.com.ua",
						"Admin"));

		assertNull(UsersUtils.getUsername());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		assertEquals("admin@sipsoft.com.ua", UsersUtils.getUsername());
	}

	@Test
	@DisplayName("Testing UserUtils that Email and Username are the same")
	void test4() {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						"admin@sipsoft.com.ua",
						"Admin"));

		assertNull(UsersUtils.getEmail());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		assertEquals(UsersUtils.getEmail(), UsersUtils.getUsername());
	}

	@Test
	@DisplayName("Testing UserUtils get User roles of logged user")
	void test5() {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						"admin@sipsoft.com.ua",
						"Admin"));
		assertTrue(UsersUtils.getUserRoles().isEmpty());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		assertTrue(CollectionUtils.isEqualCollection(UsersUtils.getUserRoles(), Set.of(
				Role.ROLE_ADMIN, Role.ROLE_CLIENT, Role.ROLE_COURIER, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
				Role.ROLE_PRODUCTOPER, Role.ROLE_USER)));
	}

	@Test
	@DisplayName("Testing UserUtils get User Highest Role")
	void test6() {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						"client@sipsoft.com.ua",
						"Client"));
		assertNull(UsersUtils.getHighestUserRole());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		assertEquals(Role.ROLE_CLIENT, UsersUtils.getHighestUserRole());
	}

	@Test
	@DisplayName("Testing UserUtils get current User")
	void test7() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		assertTrue(authentication == null || !authentication.isAuthenticated());
		authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						"admin@sipsoft.com.ua",
						"Admin"));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		assertEquals("admin@sipsoft.com.ua", UsersUtils.getEmail());
		assertEquals("admin@sipsoft.com.ua", UsersUtils.getUsername());
	}
}
