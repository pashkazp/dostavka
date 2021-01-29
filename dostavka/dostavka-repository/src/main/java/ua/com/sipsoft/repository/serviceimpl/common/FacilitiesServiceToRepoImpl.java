package ua.com.sipsoft.repository.serviceimpl.common;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddress;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.repository.common.FacilityRepository;
import ua.com.sipsoft.repository.serviceimpl.mapper.facility.FacilityMapper;
import ua.com.sipsoft.repository.serviceimpl.mapper.facility.FacilityRegDtoMapper;
import ua.com.sipsoft.service.common.FacilitiesFilter;
import ua.com.sipsoft.service.common.FacilitiesServiceToRepo;
import ua.com.sipsoft.service.dto.facility.FacilityDto;
import ua.com.sipsoft.service.dto.facility.FacilityRegistrationDto;
import ua.com.sipsoft.service.dto.facility.FacilityUpdateDto;
import ua.com.sipsoft.service.exception.ResourceNotFoundException;
import ua.com.sipsoft.service.security.UserPrincipal;
import ua.com.sipsoft.service.util.AppNotificator;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.service.util.HasFilteredList;
import ua.com.sipsoft.service.util.HasLimitedList;
import ua.com.sipsoft.service.util.HasPagingRequestToSortConvertor;
import ua.com.sipsoft.service.util.HasQueryToSortConvertor;
import ua.com.sipsoft.service.util.OffsetBasedPageRequest;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;
import ua.com.sipsoft.util.query.Query;
import ua.com.sipsoft.util.query.QuerySortOrder;
import ua.com.sipsoft.util.security.Role;

/**
 * The Class FacilitiesServiceToRepoImpl.
 *
 * @author Pavlo Degtyaryev
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FacilitiesServiceToRepoImpl
		implements FacilitiesServiceToRepo, HasQueryToSortConvertor, HasPagingRequestToSortConvertor, HasFilteredList,
		HasLimitedList {

	/** The dao. */
	private final FacilityRepository dao;

	/**
	 * Gets the by name.
	 *
	 * @param name the name
	 * @return the by name
	 */
	@Override
	public List<Facility> getByName(String name) {
		return dao.getByName(name.toLowerCase());
	}

	/**
	 * Drop links to users.
	 *
	 * @param facility the facility
	 * @param users    the users
	 * @return the optional
	 */
	@Override
	public Optional<Facility> dropLinksToUsers(Facility facility, Collection<User> users) {
		log.info("Drop Links To Users from Facility: {}", facility);
		if (facility == null || users == null || users.isEmpty()) {
			log.warn("Drop impossible. Missing some data. ");
			return Optional.empty();
		}
		for (User user : users) {
			facility.removeUser(user);
		}
		try {
			return saveFacility(facility);
		} catch (ObjectOptimisticLockingFailureException e) {
			AppNotificator.notifyError("ObjectOptimisticLockingFailureException");
			return Optional.empty();
		}
	}

	/**
	 * Adds the links to users.
	 *
	 * @param facility the facility
	 * @param users    the users
	 * @return the optional
	 */
	@Override
	public Optional<Facility> addLinksToUsers(Facility facility, Collection<User> users) {
		log.info("Add Links from Users to Facility: {}", facility);
		if (facility == null || users == null || users.isEmpty()) {
			log.warn("Add is impossible. Missing some data. ");
			return Optional.empty();
		}
		for (User user : users) {
			facility.addUser(user);
		}
		try {
			return saveFacility(facility);
		} catch (Exception e) {
			AppNotificator.notifyError(e.toString());
			return Optional.empty();
		}
	}

	/**
	 * Save.
	 *
	 * @param facility the facility
	 * @return the optional
	 */
	@Override
	public Optional<Facility> saveFacility(Facility facility) {
		log.info("Save Facility: {}", facility);
		if (facility == null) {
			log.warn("Save impossible. Missing some data. ");
			return Optional.empty();
		}
		return Optional.of(dao.saveAndFlush(facility));
	}

	/**
	 * Delete.
	 *
	 * @param facility the facility
	 */
	@Override
	public void delete(Facility facility) {
		log.info("Delete Facility: {}", facility);
		if (facility == null) {
			log.warn("Delete impossible. Missing some data. ");
		}
		try {
			dao.delete(facility);
		} catch (Exception e) {
			AppNotificator.notifyError(e.toString());
		}
	}

	/**
	 * Adds the addr to facility.
	 *
	 * @param facility the facility
	 * @param address  the address
	 * @return the optional
	 */
	@Override
	public Optional<Facility> addAddrToFacility(Facility facility, FacilityAddress address) {
		log.info("Add Addr [{}] to Facility: [{}]", address, facility);
		if (facility == null || address == null) {
			log.warn("Addition impossible. Missing some data. ");
			return Optional.empty();
		}
		address.setFacility(facility);
		facility.addFacilityAddress(address);
		try {
			return saveFacility(facility);
		} catch (Exception e) {
			AppNotificator.notifyError(e.toString());
			return Optional.empty();
		}
	}

	/**
	 * Del addr from facility.
	 *
	 * @param facility the facility
	 * @param address  the address
	 * @return the optional
	 */
	@Override
	public Optional<Facility> delAddrFromFacility(Facility facility, FacilityAddress address) {
		log.info("Remove Addr [{}] from Facility: [{}]", address, facility);
		if (facility == null || address == null) {
			log.warn("Remove is impossible. Missing some data. ");
			return Optional.empty();
		}
		facility.delFacilityAddress(address);
		try {
			return saveFacility(facility);
		} catch (Exception e) {
			AppNotificator.notifyError(e.toString());
			return Optional.empty();
		}
	}

	/**
	 * Gets the queried facilities stream.
	 *
	 * @param query the query
	 * @param value the value
	 * @return the queried facilities stream
	 */
	public Stream<Facility> getQueriedFacilitiesStream(Query<Facility, Void> query, String value) {
		log.debug("Get requested page Facilitys by Name '{}' with offset {} and limit {}", value, query.getOffset(),
				query.getLimit());
		try {
			Pageable pageable = new OffsetBasedPageRequest(query.getOffset(), query.getLimit(), queryToSort(query));
			return dao
					.getByName(value, pageable).stream();
		} catch (Exception e) {
			log.error("The Facilitys list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();
	}

	/**
	 * Gets the queried facilities.
	 *
	 * @param query the query
	 * @param value the value
	 * @return the queried facilities
	 */
	@Override
	public Stream<Facility> getQueriedFacilities(Query<Facility, Void> query, String value) {
		return getQueriedFacilitiesStream(query, value);

	}

	/**
	 * Gets the queried facilities count.
	 *
	 * @param query the query
	 * @param value the value
	 * @return the queried facilities count
	 */
	@Override
	public int getQueriedFacilitiesCount(Query<Facility, Void> query, String value) {
		return ((Long) getQueriedFacilitiesStream(query, value).count()).intValue();
	}

	/**
	 * Gets the ordered filtered facilities.
	 *
	 * @param filter the filter
	 * @param offset the offset
	 * @param limit  the limit
	 * @return the ordered filtered facilities
	 */
	@Override
	public Stream<Facility> getOrderedFilteredFacilities(String filter, int offset, int limit) {
		return getQueriedFacilitiesStream(new Query<>(offset, limit, QuerySortOrder.asc("name").build(), null, null),
				filter);
	}

	/**
	 * Gets the ordered filtered facilities count.
	 *
	 * @param filter the filter
	 * @return the ordered filtered facilities count
	 */
	@Override
	public int getOrderedFilteredFacilitiesCount(String filter) {
		return dao.getByName(filter).size();
	}

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	@Override
	public Optional<Facility> fetchById(Long id) {
		log.debug("Get Facility by id: '{}'", id);
		if (id == null) {
			log.debug("Get Facility by id is impossible. id is null.");
			return Optional.empty();
		}
		try {
			return dao.findById(id);
		} catch (Exception e) {
			log.error("The Facility by id is not received for a reason: {}", e.getMessage());
			return Optional.empty();
		}
	}

	/**
	 * Fetch FacilityDto by id.
	 *
	 * @param id the Facility id
	 * @return the optional of FacilityDto
	 */
	@Override
	public Optional<FacilityDto> fetchByIdDto(Long id) {
		log.debug("fetchByIdDto] - Get Facility by id: '{}'", id);
		if (id == null) {
			log.debug("Get Facility by id is impossible. id is null.");
			return Optional.empty();
		}

		Optional<Facility> facilityO = fetchById(id);

		return Optional.of(Mappers.getMapper(FacilityMapper.class).toDto(facilityO.get()));
	}

	/**
	 * Checks if is entity pass filter.
	 *
	 * @param entity the entity
	 * @param filter the filter
	 * @return true, if is entity pass filter
	 */
	private boolean isEntityPassFilter(Facility entity, FacilitiesFilter filter) {
		if ((filter.getUsers() != null) && !(CollectionUtils.containsAny(filter.getUsers(), entity.getUsers()))) {
			return false;
		}
		return entity.getName().toLowerCase()
				.contains(filter.getName() == null ? "" : filter.getName().toLowerCase());
	}

	/**
	 * Gets the raw queried facilities.
	 *
	 * @param query the query
	 * @return the raw queried facilities
	 */
	private Stream<Facility> getRawQueriedFacilities(Query<Facility, EntityFilter<Facility>> query) {
		log.debug(
				"Get requested page Facilities with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		if (query == null || query.getFilter().isEmpty()) {
			log.debug("Get Facilities is impossible. Miss some data.");
			return Stream.empty();
		}
		try {
			return dao.findAll(queryToSort(query))
					.stream()
					.filter(entity -> query.getFilter().get().isPass(entity));
		} catch (Exception e) {
			log.error("The Facilities list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();

	}

	/**
	 * Gets the queried facilities.
	 *
	 * @param query the query
	 * @return the queried facilities
	 */
	@Override
	public List<Facility> getQueriedFacilities(Query<Facility, EntityFilter<Facility>> query) {
		log.debug(
				"Get requested page Facilities with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		return getRawQueriedFacilities(query)
				.skip(query.getOffset())
				.limit(query.getLimit()).collect(Collectors.toList());
	}

	/**
	 * Gets the queried facilities count.
	 *
	 * @param query the query
	 * @return the queried facilities count
	 */
	@Override
	public int getQueriedFacilitiesCount(Query<Facility, EntityFilter<Facility>> query) {
		log.debug("Get requested size Facilities with filter '{}'", query.getFilter().get().toString());
		return (int) getRawQueriedFacilities(query).count();
	}

	/**
	 * Gets the queried facilities dto.
	 *
	 * @param query the query
	 * @return the queried facilities dto
	 */
	@Override
	public List<FacilityDto> getQueriedFacilitiesDto(Query<Facility, EntityFilter<Facility>> query) {
		log.debug(
				"Get requested page Facilities with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		return Mappers.getMapper(FacilityMapper.class).toDto(getQueriedFacilities(query));
//		return FacilityMapper.MAPPER.toDto(getQueriedFacilities(query));
	}

	/**
	 * Gets the filtered page.
	 *
	 * @param pagingRequest the paging request
	 * @param entityFilter  the entity filter
	 * @return the filtered page
	 */
	@Override
	public Page<FacilityDto> getFilteredPage(PagingRequest pagingRequest, EntityFilter<Facility> entityFilter) {
		log.debug(
				"IN getFilteredPage - Get requested page Facilities with PagingRequest '{}' and EntityFilter<Facility> '{}'",
				pagingRequest, entityFilter);

		Page<FacilityDto> page = new Page<>();
		List<Facility> facilities;
		User caller = null;
		if (entityFilter instanceof FacilitiesFilter) {
			caller = ((FacilitiesFilter) entityFilter).getCaller();
		}
		if (caller != null
				&& caller.hasNoOneRole(Role.ROLE_ADMIN, Role.ROLE_COURIER,
						Role.ROLE_DISPATCHER, Role.ROLE_MANAGER, Role.ROLE_PRODUCTOPER)) {
			facilities = dao.getAllOwnedByUser(caller, toSort(pagingRequest));

		} else {
			facilities = dao.findAll(toSort(pagingRequest));
		}
		page.setRecordsTotal(facilities.size());

		facilities = getFiteredList(facilities, entityFilter);

		page.setRecordsFiltered(facilities.size());

		facilities = getLimitedList(facilities, pagingRequest.getStart(), pagingRequest.getLength());

		page.setData(Mappers.getMapper(FacilityMapper.class).toDto(facilities));

		page.setDraw(pagingRequest.getDraw());

		return page;
	}

	/**
	 * Gets the all facilities.
	 *
	 * @return the all facilities
	 */
	@Override
	@PreAuthorize("isAuthenticated()")
	public List<Facility> getAllFacilities() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || auth.getPrincipal() == null) {
			return List.of();
		}
		UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
		if (CollectionUtils.containsAny(auth.getAuthorities(), AuthorityUtils.createAuthorityList(
				Role.ROLE_ADMIN.name(), Role.ROLE_COURIER.name(), Role.ROLE_DISPATCHER.name(), Role.ROLE_MANAGER.name(),
				Role.ROLE_PRODUCTOPER.name()))) {
			List<Facility> facilities = dao.findAll();
			return facilities;

		} else if (CollectionUtils.containsAny(auth.getAuthorities(), AuthorityUtils.createAuthorityList(
				Role.ROLE_CLIENT.name()))) {
			List<Facility> facilities = dao.getAllOwnedByUser(userPrincipal.getUser());
			return facilities;

		}
		return List.of();
	}

	/**
	 * Gets the all facilities dto.
	 *
	 * @return the all facilities dto
	 */
	@Override
	public List<FacilityDto> getAllFacilitiesDto() {
		List<Facility> facilities = getAllFacilities();
		List<FacilityDto> facilitiesDto = Mappers.getMapper(FacilityMapper.class).toDto(facilities);
//		List<FacilityDto> facilitiesDto = FacilityMapper.MAPPER.toDto(facilities);
		return facilitiesDto;
	}

	/**
	 * Register new facility.
	 *
	 * @param facilityRegDto the facility registration dto
	 * @return the optional
	 */
	@Override
	public Optional<FacilityDto> registerNewFacility(@NotNull FacilityRegistrationDto facilityRegDto) {

		log.info("IN registerNewFacility - Save Facility: {}", facilityRegDto);

		Facility facility = Mappers.getMapper(FacilityRegDtoMapper.class).fromRegDto(facilityRegDto);
		Optional<Facility> fasilityO = saveFacility(facility);
		if (fasilityO.isPresent()) {
			return Optional.of(Mappers.getMapper(FacilityMapper.class).toDto(fasilityO.get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<FacilityDto> updateFacility(@NotNull FacilityUpdateDto facilityUpdDto) {

		log.info("updateFacility] - Update Facility: {}", facilityUpdDto);

		Optional<Facility> facilityO = fetchById(facilityUpdDto.getId());

		if (facilityO.isEmpty()) {
			ResourceNotFoundException ex = new ResourceNotFoundException("User", "id", facilityUpdDto.getId());
			throw ex;
		}

		Facility facility = facilityO.get();

		if (StringUtils.isNotBlank(facilityUpdDto.getName())) {
			facility.setName(facilityUpdDto.getName());
		}

		facility = dao.saveAndFlush(facility);
		return Optional.of(FacilityMapper.MAPPER.toDto(facility));
	}

	@Override
	public void delete(Long facilityId) {
		log.info("delete] - Try to delete facility by Id: {}", facilityId);
		if (facilityId == null) {
			log.debug("Delete Facilities is impossible. Miss some data.");
		}

		Optional<Facility> facility = dao.findById(facilityId);
		// TODO perform a check for the possibility of deleting the Facility
		if (facility.isPresent()) {
			dao.delete(facility.get());
		}

	}

}