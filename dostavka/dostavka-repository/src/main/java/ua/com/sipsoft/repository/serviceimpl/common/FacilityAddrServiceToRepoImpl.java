package ua.com.sipsoft.repository.serviceimpl.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddr;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.repository.common.FacilityAddrRepo;
import ua.com.sipsoft.repository.serviceimpl.mapper.facility.FacilityAddMapper;
import ua.com.sipsoft.service.common.FacilitiesServiceToRepo;
import ua.com.sipsoft.service.common.FacilityAddrFilter;
import ua.com.sipsoft.service.common.FacilityAddrServiceToRepo;
import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrUpdReqDto;
import ua.com.sipsoft.service.exception.FacilityAddrNotFoundException;
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
	private final FacilityAddrRepo dao;

	private final FacilitiesServiceToRepo facilityService;
	private final I18NProvider i18n;
	private final FacilityAddMapper facilityAddMapper;

	/**
	 * Gets the facility addresses.
	 *
	 * @param facility the facility
	 * @return the facility addresses
	 */
	@Override
	public List<FacilityAddr> getFacilityAddr(Facility facility) {
		return dao.findByFacility(facility);
	}

	/**
	 * Save.
	 *
	 * @param facilityAddress the facility address
	 * @return the optional
	 */
	@Override
	public Optional<FacilityAddr> save(FacilityAddr facilityAddress) {
		log.info("Save Facility address: {}", facilityAddress);
		if (facilityAddress == null) {
			log.warn("Save impossible. Missing some data. ");
			return Optional.empty();
		}
		return Optional.ofNullable(dao.saveAndFlush(facilityAddress));
	}

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	@Override
	public Optional<FacilityAddr> fetchById(Long id) {
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
	private boolean isEntityPassFilter(FacilityAddr entity, FacilityAddrFilter filter) {
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
	public Stream<FacilityAddr> getQueriedFacilityAddrByFilter(
			Query<FacilityAddr, EntityFilter<FacilityAddr>> query, Long facilityId) {
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
	public int getQueriedFacilityAddrByFilterCount(Query<FacilityAddr, EntityFilter<FacilityAddr>> query,
			Long faciliyId) {
		log.debug("Get requested size Facility Addresses  with filter '{}'", query.getFilter().get().toString());
		return (int) getQueriedFacilityAddrByFilter(query, faciliyId).count();
	}

	@Override
	public List<FacilityAddr> getAllFacilityAddr() {
		List<FacilityAddr> facilityAddresses = dao.findAll();
		return facilityAddresses;
	}

	private List<FacilityAddr> getAllFacilityAddr(Long facilityUserId) {
		List<FacilityAddr> facilityAddresses = dao.getdAllWithUserId(facilityUserId);
		return facilityAddresses;
	}

	@Override
	public List<FacilityAddrDto> getAllFacilityAddrDto() {
		List<FacilityAddr> facilityAddresses = getAllFacilityAddr();
		List<FacilityAddrDto> facilityAddressesDto = facilityAddMapper.toDto(facilityAddresses);
		return facilityAddressesDto;
	}

	@Override
	public List<FacilityAddrDto> getAllFacilityAddrDto(@NonNull Long facilityUserId) {
		List<FacilityAddr> facilityAddresses = getAllFacilityAddr(facilityUserId);
		List<FacilityAddrDto> facilityAddressesDto = facilityAddMapper.toDto(facilityAddresses);
		return facilityAddressesDto;
	}

	@Override
	public Optional<FacilityAddrDto> fetchByIdDto(@NonNull Long id) {
		Optional<FacilityAddrDto> facilityAddrDtoO;
		Optional<FacilityAddr> facilityAddrO = fetchById(id);
		if (facilityAddrO.isPresent()) {
			facilityAddrDtoO = Optional
					.ofNullable(facilityAddMapper.toDto(facilityAddrO.get()));
//			facilityAddrDtoO = Optional.of(FacilityAddMapper.MAPPER.toDto(facilityAddrO.get()));
		} else {
			facilityAddrDtoO = Optional.empty();
		}
		return facilityAddrDtoO;
	}

	@Override
	public Optional<FacilityAddrDto> fetchByIdDtoWithUser(@NonNull Long id, @NonNull User caller) {
		Optional<FacilityAddrDto> facilityAddrDtoO;
		Optional<FacilityAddr> facilityAddrO = fetchById(id);
		if (facilityAddrO.isPresent() && facilityAddrO.get().getFacility() != null
				&& facilityAddrO.get().getFacility().getUsers().contains(caller)) {
			facilityAddrDtoO = Optional
					.ofNullable(facilityAddMapper.toDto(facilityAddrO.get()));
		} else {
			facilityAddrDtoO = Optional.empty();
		}
		return facilityAddrDtoO;
	}

	@Override
	public Optional<FacilityAddrDto> registerNewFacilityAddress(@NonNull Long fasilityId,
			@NonNull FacilityAddrDto facilityAddressDto) {

		Optional<Facility> facilityO = facilityService.fetchById(fasilityId);
		if (facilityO.isEmpty()) {
			log.info("registerNewFacilityAddress] - Registration is fail. Inform to the registrant");

			Locale locale = LocaleContextHolder.getLocale();
			FacilityNotFoundException ex = new FacilityNotFoundException();
			ex.setErrMsg(i18n.getTranslation(RestV1Msg.FACILITY_NOTFOUND, locale));
			ex.setErrMsgExt(i18n.getTranslation(RestV1Msg.FACILITY_NOTFOUND_EXT, locale));
			throw ex;
		}

		FacilityAddr facilityAddress = facilityAddMapper.fromDto(facilityAddressDto);
		Set<FacilityAddr> oldList = facilityO.get().getFacilityAddresses().stream().collect(Collectors.toSet());
		facilityO.get().addFacilityAddress(facilityAddress);
		facilityO = facilityService.saveFacility(facilityO.get());

		if (facilityO.isEmpty()) {
			return Optional.empty();
		} else {

			List<FacilityAddr> addrList = new ArrayList<>(
					(CollectionUtils.removeAll(facilityO.get().getFacilityAddresses(), oldList)));
			if (addrList.size() == 1) {
				return Optional.ofNullable(facilityAddMapper.toDto(addrList.get(0)));
			} else {
				return Optional.empty();
			}

		}
	}

	@Override
	public Optional<FacilityAddrDto> updateFacilityAddress(@lombok.NonNull Long fasilityId,
			@lombok.NonNull FacilityAddrUpdReqDto facilityAddrUpdReqDto) {

		log.info("updateFacilityAddress] - Update Facility address id {}: {}", fasilityId, facilityAddrUpdReqDto);

		Optional<FacilityAddr> facilityAddrO = fetchById(fasilityId);

		if (facilityAddrO.isEmpty()) {
			Locale locale = LocaleContextHolder.getLocale();
			FacilityAddrNotFoundException ex = new FacilityAddrNotFoundException();
			ex.setErrMsg(i18n.getTranslation(RestV1Msg.FACILITYADDR_NOTFOUND, locale));
			ex.setErrMsgExt(i18n.getTranslation(RestV1Msg.FACILITYADDR_NOTFOUND_EXT, locale));
			throw ex;
		}

		FacilityAddr fa = facilityAddrO.get();

		fa.setAddress(facilityAddrUpdReqDto.getAddress());
		fa.setAddressesAlias(facilityAddrUpdReqDto.getAddressesAlias());
		fa.setDefaultAddress(facilityAddrUpdReqDto.isDefaultAddress());
		fa.setLat(StringUtils.isBlank(facilityAddrUpdReqDto.getLat()) ? null
				: Double.valueOf(facilityAddrUpdReqDto.getLat()));
		fa.setLng(StringUtils.isBlank(facilityAddrUpdReqDto.getLng()) ? null
				: Double.valueOf(facilityAddrUpdReqDto.getLng()));

		fa = dao.saveAndFlush(fa);

		return Optional.ofNullable(facilityAddMapper.toDto(fa));
	}

}