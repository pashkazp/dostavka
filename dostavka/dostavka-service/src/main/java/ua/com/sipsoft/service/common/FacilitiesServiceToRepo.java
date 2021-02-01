package ua.com.sipsoft.service.common;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddress;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.facility.FacilityDto;
import ua.com.sipsoft.service.dto.facility.FacilityRegistrationDto;
import ua.com.sipsoft.service.dto.facility.FacilityUpdateDto;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;
import ua.com.sipsoft.util.query.Query;

/**
 * The Class FacilitiesServiceToRepo.
 *
 * @author Pavlo Degtyaryev
 */

@Service
public interface FacilitiesServiceToRepo {

	/**
	 * Gets the by name.
	 *
	 * @param name the name
	 * @return the by name
	 */
	List<Facility> getByName(String name);

	/**
	 * Drop links to users.
	 *
	 * @param facility the facility
	 * @param users    the users
	 * @return the optional
	 */
	Optional<Facility> dropLinksToUsers(Facility facility, Collection<User> users);

	/**
	 * Save.
	 *
	 * @param facility the facility
	 * @return the optional
	 */
	Optional<Facility> saveFacility(Facility facility);

	/**
	 * Delete.
	 *
	 * @param facility the facility
	 */
	void delete(Facility facility);

	/**
	 * Adds the addr to facility.
	 *
	 * @param facility the facility
	 * @param address  the address
	 * @return the optional
	 */
	Optional<Facility> addAddrToFacility(Facility facility, FacilityAddress address);

	/**
	 * Del addr from facility.
	 *
	 * @param facility the facility
	 * @param address  the address
	 * @return the optional
	 */
	Optional<Facility> delAddrFromFacility(Facility facility, FacilityAddress address);

	/**
	 * Adds the links to users.
	 *
	 * @param facility the facility
	 * @param users    the users
	 * @return the optional
	 */
	Optional<Facility> addLinksToUsers(Facility facility, Collection<User> users);

	/**
	 * Gets the queried facilities.
	 *
	 * @param query the query
	 * @param value the value
	 * @return the queried facilities
	 */
	Stream<Facility> getQueriedFacilities(Query<Facility, Void> query, String value);

	/**
	 * Gets the queried facilities count.
	 *
	 * @param query the query
	 * @param value the value
	 * @return the queried facilities count
	 */
	int getQueriedFacilitiesCount(Query<Facility, Void> query, String value);

	/**
	 * Gets the ordered filtered facilities.
	 *
	 * @param filter the filter
	 * @param offset the offset
	 * @param limit  the limit
	 * @return the ordered filtered facilities
	 */
	Stream<Facility> getOrderedFilteredFacilities(String filter, int offset, int limit);

	/**
	 * Gets the ordered filtered facilities count.
	 *
	 * @param filter the filter
	 * @return the ordered filtered facilities count
	 */
	int getOrderedFilteredFacilitiesCount(String filter);

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	Optional<Facility> fetchById(Long id);

	/**
	 * Fetch {@link FacilityDto}} by id.
	 *
	 * @param id the id
	 * @return the Optional FacilityDto
	 */
	Optional<FacilityDto> fetchByIdDto(Long id);

	/**
	 * Gets the queried {@link List} of {@link Facility}facilities.
	 *
	 * @param query the query
	 * @return the queried facilities
	 */
	List<Facility> getQueriedFacilities(Query<Facility, EntityFilter<Facility>> query);

	/**
	 * Gets the queried facilities count.
	 *
	 * @param query the query
	 * @return the queried facilities count
	 */
	int getQueriedFacilitiesCount(Query<Facility, EntityFilter<Facility>> query);

	/**
	 * Gets the queried facilities dto.
	 *
	 * @param query the query
	 * @return the queried facilities dto
	 */
	List<FacilityDto> getQueriedFacilitiesDto(Query<Facility, EntityFilter<Facility>> query);

	/**
	 * Gets the filtered page.
	 *
	 * @param pagingRequest  the paging request
	 * @param facilityFilter the facility filter
	 * @return the filtered page
	 */
	Page<FacilityDto> getFilteredPage(PagingRequest pagingRequest, EntityFilter<Facility> facilityFilter);

	/**
	 * Gets the all facilities.
	 *
	 * @return the all facilities
	 */
	List<Facility> getAllFacilities();

	/**
	 * Gets the all facilities dto.
	 *
	 * @return the all facilities dto
	 */
	List<FacilityDto> getAllFacilitiesDto();

	/**
	 * Register new facility.
	 *
	 * @param facilityRegDto the facility dto
	 * @return the optional
	 */
	Optional<FacilityDto> registerNewFacility(FacilityRegistrationDto facilityRegDto);

	/**
	 * Update facility.
	 *
	 * @param facilityUpdDto the facility dto
	 * @return the optional
	 */
	Optional<FacilityDto> updateFacility(@NotNull FacilityUpdateDto facilityUpdDto);

	Optional<FacilityDto> updateFacility(@NotNull FacilityDto facilityDto);

	void delete(Long facilityId);

}