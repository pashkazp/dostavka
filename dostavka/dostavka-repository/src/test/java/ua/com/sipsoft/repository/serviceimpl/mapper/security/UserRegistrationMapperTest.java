package ua.com.sipsoft.repository.serviceimpl.mapper.security;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.user.UserRegistrationDto;

class UserRegistrationMapperTest implements UserCreator {

	@Test
	@DisplayName("Map UserRegistrationDto to User")
	void test1() {
		UserRegistrationDto userRegistrationDto = createUserRegistrationDto();
		UserRegistrationMapper userRegistrationMapper = Mappers.getMapper(UserRegistrationMapper.class);
		User user = userRegistrationMapper.fromDto(userRegistrationDto);
		assertAll(
				() -> assertEquals(userRegistrationDto.getName(), user.getName()),
				() -> assertEquals(userRegistrationDto.getEmail(), user.getEmail()),
				() -> assertEquals(userRegistrationDto.getPassword(), user.getPassword()),
				() -> assertTrue(CollectionUtils.isEqualCollection(userRegistrationDto.getRoles(), user.getRoles())));
	}

	@Test
	@DisplayName("Map User to UserRegistrationDto")
	void test2() {
		User user = createUser();
		UserRegistrationMapper userRegistrationMapper = Mappers.getMapper(UserRegistrationMapper.class);
		UserRegistrationDto userRegistrationDto = userRegistrationMapper.toDto(user);
		assertAll(
				() -> assertEquals(user.getName(), userRegistrationDto.getName()),
				() -> assertEquals(user.getEmail(), userRegistrationDto.getEmail()),
				() -> assertEquals(user.getPassword(), userRegistrationDto.getPassword()),
				() -> assertTrue(CollectionUtils.isEqualCollection(user.getRoles(), userRegistrationDto.getRoles())));
	}

}
