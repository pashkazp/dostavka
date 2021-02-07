package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.user.UserRegistrationDto;
import ua.com.sipsoft.ui.model.request.user.UserRegReq;

@Mapper(componentModel = "spring")
@Component
public interface ToUserRegDtoMapper {

	UserRegistrationDto fromUserRegReq(UserRegReq newUser);
}
