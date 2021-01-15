package ua.com.sipsoft.service.dto.user;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegistrationDto extends BaseUserInfoDto implements Serializable {

	private static final long serialVersionUID = 1848596179922865880L;

}
