package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.user.UserRegistrationDto;
import ua.com.sipsoft.ui.model.request.user.UserRegistrationRequest;

@Mapper
public interface ToUserRegistrationDtoMapper {
	ToUserRegistrationDtoMapper MAPPER = Mappers
			.getMapper(ToUserRegistrationDtoMapper.class);

	UserRegistrationDto fromUserRegistrationRequest(UserRegistrationRequest newUser);
}
