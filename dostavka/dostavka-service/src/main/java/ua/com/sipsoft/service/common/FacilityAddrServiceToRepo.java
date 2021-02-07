package ua.com.sipsoft.service.common;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddr;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrUpdReqDto;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.util.query.Query;

/**
 * The Interface FacilityAddrServiceToRepo.
 *
 * @author Pavlo Degtyaryev
 */
@Service
public interface FacilityAddrServiceToRepo {

	/**
	 * Gets the facility addresses.
	 *
	 * @param facility the facility
	 * @return the facility addresses
	 */
	public List<FacilityAddr> getFacilityAddr(Facility facility);

	/**
	 * Save.
	 *
	 * @param operationData the operation data
	 * @return the optional
	 */
	Optional<FacilityAddr> save(FacilityAddr operationData);

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	public Optional<FacilityAddr> fetchById(Long id);

	/**
	 * Gets the queried facility addr by filter.
	 *
	 * @param query      the query
	 * @param facilityId the facility id
	 * @return the queried facility addr by filter
	 */
	Stream<FacilityAddr> getQueriedFacilityAddrByFilter(Query<FacilityAddr, EntityFilter<FacilityAddr>> query,
			Long facilityId);

	/**
	 * Gets the queried facility addr by filter count.
	 *
	 * @param query      the query
	 * @param facilityId the facility id
	 * @return the queried facility addr by filter count
	 */
	int getQueriedFacilityAddrByFilterCount(Query<FacilityAddr, EntityFilter<FacilityAddr>> query,
			Long facilityId);

	public List<FacilityAddr> getAllFacilityAddr();

	public List<FacilityAddrDto> getAllFacilityAddrDto();

	public Optional<FacilityAddrDto> fetchByIdDto(Long id);

	public Optional<FacilityAddrDto> fetchByIdDtoWithUser(@NonNull Long id, @NonNull User caller);

	public Optional<FacilityAddrDto> registerNewFacilityAddress(@NonNull Long fasilityId,
			@NonNull FacilityAddrDto facilityAddressDto);

	public Optional<FacilityAddrDto> updateFacilityAddress(@NonNull Long fasilityId,
			@NonNull FacilityAddrUpdReqDto facilityAddrUpdReqDto);

	public List<FacilityAddrDto> getAllFacilityAddrDto(Long facilityUserId);

}