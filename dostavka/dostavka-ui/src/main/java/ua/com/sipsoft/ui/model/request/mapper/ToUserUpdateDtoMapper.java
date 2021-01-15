package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.user.UserUpdateDto;
import ua.com.sipsoft.ui.model.request.user.UserUpdateRequest;

@Mapper
public interface ToUserUpdateDtoMapper {
	ToUserUpdateDtoMapper MAPPER = Mappers
			.getMapper(ToUserUpdateDtoMapper.class);

	UserUpdateDto fromUserUpdateRequest(UserUpdateRequest newUser);
}
