package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.user.UserUpdateDto;
import ua.com.sipsoft.ui.model.request.user.UserUpdateRequest;

@Mapper(componentModel = "spring")
@Component
public interface ToUserUpdateDtoMapper {

	UserUpdateDto fromUserUpdateRequest(UserUpdateRequest newUser);
}
