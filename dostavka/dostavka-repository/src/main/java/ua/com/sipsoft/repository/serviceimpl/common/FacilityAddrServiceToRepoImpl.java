package ua.com.sipsoft.repository.serviceimpl.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddress;
import ua.com.sipsoft.repository.common.FacilityAddressRepository;
import ua.com.sipsoft.repository.serviceimpl.mapper.facility.FacilityAddressMapper;
import ua.com.sipsoft.service.common.FacilitiesServiceToRepo;
import ua.com.sipsoft.service.common.FacilityAddrServiceToRepo;
import ua.com.sipsoft.service.common.FacilityAddressFilter;
import ua.com.sipsoft.service.dto.facility.FacilityAddressDto;
import ua.com.sipsoft.service.exception.FacilityNotFoundException;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.service.util.HasQueryToSortConvertor;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.message.RestV1Msg;
import ua.com.sipsoft.util.query.Query;

/**
 * The Class FacilityAddrServiceToRepoImpl.
 *
 * @author Pavlo Degtyaryev
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FacilityAddrServiceToRepoImpl implements FacilityAddrServiceToRepo, HasQueryToSortConvertor {

	/** The dao. */
	private final FacilityAddressRepository dao;

	private final FacilitiesServiceToRepo facilityService;
	private final I18NProvider i18n;

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
	public Optional<FacilityAddressDto> registerNewFacilityAddress(@NonNull Long fasilityId,
			@NonNull FacilityAddressDto facilityAddressDto) {

		Optional<Facility> facilityO = facilityService.fetchById(fasilityId);
		if (facilityO.isEmpty()) {
			log.info("registerNewFacilityAddress] - Registration is fail. Inform to the registrant");

			Locale locale = LocaleContextHolder.getLocale();
			FacilityNotFoundException ex = new FacilityNotFoundException();
			ex.setErrMsg(i18n.getTranslation(RestV1Msg.FACILITY_NOTFOUND, locale));
			ex.setErrMsgExt(i18n.getTranslation(RestV1Msg.FACILITY_NOTFOUND_EXT, locale));
			throw ex;
		}

		FacilityAddress facilityAddress = Mappers.getMapper(FacilityAddressMapper.class).fromDto(facilityAddressDto);
		Set<FacilityAddress> oldList = facilityO.get().getFacilityAddresses().stream().collect(Collectors.toSet());
		facilityO.get().addFacilityAddress(facilityAddress);
		facilityO = facilityService.saveFacility(facilityO.get());

		if (facilityO.isEmpty()) {
			return Optional.empty();
		} else {

			List<FacilityAddress> addrList = new ArrayList<>(
					(CollectionUtils.removeAll(facilityO.get().getFacilityAddresses(), oldList)));
			if (addrList.size() == 1) {
				return Optional.of(Mappers.getMapper(FacilityAddressMapper.class).toDto(addrList.get(0)));
			} else {
				return Optional.empty();
			}

		}
	}

}