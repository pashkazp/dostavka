package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.user.UserRegistrationDto;
import ua.com.sipsoft.ui.model.request.user.UserRegReq;

@Mapper
public interface ToUserRegDtoMapper {
	ToUserRegDtoMapper MAPPER = Mappers
			.getMapper(ToUserRegDtoMapper.class);

	UserRegistrationDto fromUserRegReq(UserRegReq newUser);
}
