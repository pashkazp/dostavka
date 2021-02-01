package ua.com.sipsoft.service.common;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddress;
import ua.com.sipsoft.service.dto.facility.FacilityAddressDto;
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
	public List<FacilityAddress> getFacilityAddresses(Facility facility);

	/**
	 * Save.
	 *
	 * @param operationData the operation data
	 * @return the optional
	 */
	Optional<FacilityAddress> save(FacilityAddress operationData);

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	public Optional<FacilityAddress> fetchById(Long id);

	/**
	 * Gets the queried facility addr by filter.
	 *
	 * @param query      the query
	 * @param facilityId the facility id
	 * @return the queried facility addr by filter
	 */
	Stream<FacilityAddress> getQueriedFacilityAddrByFilter(Query<FacilityAddress, EntityFilter<FacilityAddress>> query,
			Long facilityId);

	/**
	 * Gets the queried facility addr by filter count.
	 *
	 * @param query      the query
	 * @param facilityId the facility id
	 * @return the queried facility addr by filter count
	 */
	int getQueriedFacilityAddrByFilterCount(Query<FacilityAddress, EntityFilter<FacilityAddress>> query,
			Long facilityId);

	public List<FacilityAddress> getAllFacilityAddr();

	public List<FacilityAddressDto> getAllFacilityAddrDto();

	public Optional<FacilityAddressDto> fetchByIdDto(Long id);

	public Optional<FacilityAddressDto> registerNewFacilityAddress(@NonNull Long fasilityId,
			@NonNull FacilityAddressDto facilityAddressDto);

}