package ua.com.sipsoft.repository.serviceimpl.mapper.security;

import java.util.Collections;

import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.user.UserDto;
import ua.com.sipsoft.service.dto.user.UserRegistrationDto;
import ua.com.sipsoft.util.security.Role;

public interface UserCreator {
	default User createUser() {
		User user = new User();
		user.setEmail("email@email.email");
		user.setEnabled(true);
		user.setId(13l);
		user.setName("User Name");
		user.setPassword("$2a$10$43pB8uxi6SOmI1");
		user.setRoles(Collections.singleton(Role.ROLE_ADMIN));
		user.setVerified(true);
		user.setVersion(13l);

		return user;
	}

	default UserDto createUserDto() {
		UserDto userDto = new UserDto();
		userDto.setEmail("email@email.email");
		userDto.setEnabled(true);
		userDto.setId(13l);
		userDto.setName("User Name");
		userDto.setPassword("$2a$10$43pB8uxi6SOmI1");
		userDto.setRoles(Collections.singleton(Role.ROLE_ADMIN));
		userDto.setVerified(true);
		userDto.setVersion(13l);

		return userDto;
	}

	default UserRegistrationDto createUserRegistrationDto() {
		UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
		userRegistrationDto.setName("User Name");
		userRegistrationDto.setPassword("TruePassword1");
		userRegistrationDto.setEmail("email@email.email");
		userRegistrationDto.setRoles(Collections.singleton(Role.ROLE_ADMIN));

		return userRegistrationDto;
	}
}
