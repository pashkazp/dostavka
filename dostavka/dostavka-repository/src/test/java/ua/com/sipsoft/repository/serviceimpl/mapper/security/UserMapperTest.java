package ua.com.sipsoft.repository.serviceimpl.mapper.security;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.user.UserDto;

class UserMapperTest implements UserCreator {

	@Test
	@DisplayName("Map User to UserDto")
	void test1() {
		User user = createUser();
		UserMapper userMapper = Mappers.getMapper(UserMapper.class);
		UserDto userDto = userMapper.toDto(user);
		assertAll(
				() -> assertEquals(userDto.getEmail(), user.getEmail()),
				() -> assertEquals(userDto.getEnabled(), user.getEnabled()),
				() -> assertEquals(userDto.getId(), user.getId()),
				() -> assertEquals(userDto.getName(), user.getName()),
				() -> assertEquals(userDto.getPassword(), user.getPassword()),
				() -> assertEquals(userDto.getVerified(), user.getVerified()),
				() -> assertEquals(userDto.getVersion(), user.getVersion()),
				() -> assertTrue(CollectionUtils.isEqualCollection(userDto.getRoles(), user.getRoles())));
	}

	@Test
	@DisplayName("Map UserDto to User")
	void test2() {
		UserDto userDto = createUserDto();
		UserMapper userMapper = Mappers.getMapper(UserMapper.class);
		User user = userMapper.fromDto(userDto);
		assertAll(
				() -> assertEquals(user.getEmail(), userDto.getEmail()),
				() -> assertEquals(user.getEnabled(), userDto.getEnabled()),
				() -> assertEquals(user.getId(), userDto.getId()),
				() -> assertEquals(user.getName(), userDto.getName()),
				() -> assertEquals(user.getPassword(), userDto.getPassword()),
				() -> assertEquals(user.getVerified(), userDto.getVerified()),
				() -> assertEquals(user.getVersion(), userDto.getVersion()),
				() -> assertTrue(CollectionUtils.isEqualCollection(user.getRoles(), userDto.getRoles())));
	}

	@Test
	@DisplayName("Copy User to User")
	void test3() {
		User user = createUser();
		User userCopy = Mappers.getMapper(UserMapper.class).getCopy(user);
		assertAll(
				() -> assertEquals(user.getEmail(), userCopy.getEmail()),
				() -> assertEquals(user.getEnabled(), userCopy.getEnabled()),
				() -> assertEquals(user.getId(), userCopy.getId()),
				() -> assertEquals(user.getName(), userCopy.getName()),
				() -> assertEquals(user.getPassword(), userCopy.getPassword()),
				() -> assertEquals(user.getVerified(), userCopy.getVerified()),
				() -> assertEquals(user.getVersion(), userCopy.getVersion()),
				() -> assertTrue(CollectionUtils.isEqualCollection(user.getRoles(), userCopy.getRoles())));
	}

}
