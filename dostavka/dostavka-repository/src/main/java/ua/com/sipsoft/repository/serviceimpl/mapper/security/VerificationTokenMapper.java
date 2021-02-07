package ua.com.sipsoft.repository.serviceimpl.mapper.security;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.dao.common.VerificationToken;
import ua.com.sipsoft.service.dto.VerificationTokenDto;

/**
 * Implementing the MapStruct interface in VerificationTokenMapper for mutual
 * conversion of {@link VerificationToken} and {@link VerificationTokenDto}
 */
@Mapper(componentModel = "spring")
@Component

public interface VerificationTokenMapper {

	/**
	 * From {@link VerificationTokenDto} to {@link VerificationToken}.
	 *
	 * @param verificationTokenDto the verification token dto
	 * @return the {@link VerificationToken}
	 */
	VerificationToken fromDto(VerificationTokenDto verificationTokenDto);

	/**
	 * From {@link VerificationToken} to {@link VerificationTokenDto}
	 *
	 * @param verificationToken the verification token
	 * @return the {@link VerificationTokenDto}
	 */
	VerificationTokenDto toDto(VerificationToken verificationToken);
}
