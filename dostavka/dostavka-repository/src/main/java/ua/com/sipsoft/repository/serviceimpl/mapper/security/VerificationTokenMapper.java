package ua.com.sipsoft.repository.serviceimpl.mapper.security;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.dao.common.VerificationToken;
import ua.com.sipsoft.service.dto.VerificationTokenDto;

/**
 * Implementing the MapStruct interface in VerificationTokenMapper for mutual
 * conversion of {@link VerificationToken} and {@link VerificationTokenDto}
 */
@Mapper

public interface VerificationTokenMapper {

	/** The Constant MAPPER. */
	VerificationTokenMapper MAPPER = Mappers.getMapper(VerificationTokenMapper.class);

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
