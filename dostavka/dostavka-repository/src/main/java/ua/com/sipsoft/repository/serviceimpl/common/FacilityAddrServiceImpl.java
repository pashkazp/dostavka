package ua.com.sipsoft.repository.serviceimpl.common;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.mapstruct.factory.Mappers;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddress;
import ua.com.sipsoft.repository.common.FacilityAddressRepository;
import ua.com.sipsoft.repository.serviceimpl.mapper.facility.FacilityAddressMapper;
import ua.com.sipsoft.service.common.FacilitiesService;
import ua.com.sipsoft.service.common.FacilityAddrService;
import ua.com.sipsoft.service.common.FacilityAddressFilter;
import ua.com.sipsoft.service.dto.facility.FacilityAddressDto;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.service.util.HasQueryToSortConvertor;
import ua.com.sipsoft.util.query.Query;

/**
 * The Class FacilityAddrServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FacilityAddrServiceImpl implements FacilityAddrService, HasQueryToSortConvertor {

	/** The dao. */
	private final FacilityAddressRepository dao;

	private final FacilitiesService facilityService;

	/**
	 * Gets the facility addresses.
	 *
	 * @param facility the facility
	 * @return the facility addresses
	 */
	@Override
	public List<FacilityAddress> getFacilityAddresses(Facility facility) {
		return dao.findByFacility(facility);
	}

	/**
	 * Save.
	 *
	 * @param facilityAddress the facility address
	 * @return the optional
	 */
	@Override
	public Optional<FacilityAddress> save(FacilityAddress facilityAddress) {
		log.info("Save Facility address: {}", facilityAddress);
		if (facilityAddress == null) {
			log.warn("Save impossible. Missing some data. ");
			return Optional.empty();
		}
		return Optional.of(dao.saveAndFlush(facilityAddress));
	}

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	@Override
	public Optional<FacilityAddress> fetchById(Long id) {
		log.debug("Get Facility Address by id: '{}'", id);
		if (id == null) {
			log.debug("Get Facility Address by id is impossible. id is null.");
			return Optional.empty();
		}
		try {
			return dao.findById(id);
		} catch (Exception e) {
			log.error("The Facility Address by id is not received for a reason: {}", e.getMessage());
			return Optional.empty();
		}
	}

	/**
	 * Checks if is entity pass filter.
	 *
	 * @param entity the entity
	 * @param filter the filter
	 * @return true, if is entity pass filter
	 */
	private boolean isEntityPassFilter(FacilityAddress entity, FacilityAddressFilter filter) {
		return entity.getAddress().concat(entity.getAddressesAlias()).toLowerCase()
				.contains(filter.getAddress() == null ? "" : filter.getAddress().toLowerCase());
	}

	/**
	 * Gets the queried facility addr by filter.
	 *
	 * @param query      the query
	 * @param facilityId the facility id
	 * @return the queried facility addr by filter
	 */
	@Override
	public Stream<FacilityAddress> getQueriedFacilityAddrByFilter(
			Query<FacilityAddress, EntityFilter<FacilityAddress>> query, Long facilityId) {
		log.debug("Get requested page Facility Addresses with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		if (query == null || query.getFilter().isEmpty()) {
			log.debug("Get Drafr Facility Addresses is impossible. Miss some data.");
			return Stream.empty();
		}
		try {
			return dao.getByFacilityId(facilityId, queryToSort(query))
					.stream()
					.filter(entity -> query.getFilter().get().isPass(entity))
					.skip(query.getOffset())
					.limit(query.getLimit());
		} catch (Exception e) {
			log.error("The Facility Addresses list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();
	}

	/**
	 * Gets the queried facility addr by filter count.
	 *
	 * @param query     the query
	 * @param faciliyId the faciliy id
	 * @return the queried facility addr by filter count
	 */
	@Override
	public int getQueriedFacilityAddrByFilterCount(Query<FacilityAddress, EntityFilter<FacilityAddress>> query,
			Long faciliyId) {
		log.debug("Get requested size Facility Addresses  with filter '{}'", query.getFilter().get().toString());
		return (int) getQueriedFacilityAddrByFilter(query, faciliyId).count();
	}

	@Override
	public List<FacilityAddress> getAllFacilityAddr() {
		List<FacilityAddress> facilityAddresses = dao.findAll();
		return facilityAddresses;
	}

	@Override
	public List<FacilityAddressDto> getAllFacilityAddrDto() {
		List<FacilityAddress> facilityAddresses = getAllFacilityAddr();
		List<FacilityAddressDto> facilityAddressesDto = Mappers.getMapper(FacilityAddressMapper.class)
				.toDto(facilityAddresses);
//		List<FacilityAddressDto> facilityAddressesDto = FacilityAddressMapper.MAPPER.toDto(facilityAddresses);
		return facilityAddressesDto;
	}

	@Override
	public Optional<FacilityAddressDto> fetchByIdDto(Long id) {
		Optional<FacilityAddressDto> facilityAddrDtoO;
		Optional<FacilityAddress> facilityAddrO = fetchById(id);
		if (facilityAddrO.isPresent()) {
			facilityAddrDtoO = Optional.of(Mappers.getMapper(FacilityAddressMapper.class).toDto(facilityAddrO.get()));
//			facilityAddrDtoO = Optional.of(FacilityAddressMapper.MAPPER.toDto(facilityAddrO.get()));
		} else {
			facilityAddrDtoO = Optional.empty();
		}
		return facilityAddrDtoO;
	}

	@Override
	public Optional<FacilityAddressDto> registerNewFacilityAddress(@NonNull FacilityAddressDto facilityAddrDto,
			@NonNull Long fasilityId) {

		Optional<Facility> facilityO = facilityService.fetchById(fasilityId);
		if (facilityO.isEmpty()) {
			return Optional.empty();
		}

		FacilityAddress facilityAddress = Mappers.getMapper(FacilityAddressMapper.class).fromDto(facilityAddrDto);
//		FacilityAddress facilityAddress = FacilityAddressMapper.MAPPER.fromDto(facilityAddrDto);
		facilityAddress.setFacility(facilityO.get());

		facilityAddress = dao.saveAndFlush(facilityAddress);
		if (facilityAddress == null) {
			return Optional.empty();
		}

		facilityO.get().addFacilityAddress(facilityAddress);
		facilityO = facilityService.saveFacility(facilityO.get());

		if (facilityO.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(Mappers.getMapper(FacilityAddressMapper.class).toDto(facilityAddress));
//			return Optional.of(FacilityAddressMapper.MAPPER.toDto(facilityAddress));
		}
	}

}