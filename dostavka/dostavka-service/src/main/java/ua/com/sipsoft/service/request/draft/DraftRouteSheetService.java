package ua.com.sipsoft.service.request.draft;

import java.util.Locale;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.request.DraftSheetUpdReqDto;
import ua.com.sipsoft.service.dto.request.HistoryEventDto;
import ua.com.sipsoft.service.dto.request.RouteSheetDto;
import ua.com.sipsoft.service.dto.user.UserDto;
import ua.com.sipsoft.service.exception.FacilityDtoAuditExeption;
import ua.com.sipsoft.service.security.UserDtoMapper;
import ua.com.sipsoft.service.security.UserPrincipal;
import ua.com.sipsoft.service.util.audit.RouteSheetUpdReqDtoAuditor;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.message.RestV1Msg;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftRouteSheetService {

	private final DraftRouteSheetServiceToRepo draftRouteSheetServiceToRepo;
	private final RouteSheetUpdReqDtoAuditor routeSheetUpdReqDtoAuditor;
	private final I18NProvider i18n;
	private final UserDtoMapper userDtoMapper;

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER" })
	public Page<RouteSheetDto> getFilteredPage(@NonNull PagingRequest pagingRequest,
			@NonNull DraftSheetFilter draftSheetFilter) {
		return draftRouteSheetServiceToRepo.getFilteredPage(pagingRequest, draftSheetFilter);
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER" })
	public Optional<RouteSheetDto> getRouteSheetDto(@NonNull Long sheetId) {
		return draftRouteSheetServiceToRepo.fetchById(sheetId);
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_PRODUCTOPER" })
	public Optional<RouteSheetDto> updateRouteSheet(@NonNull DraftSheetUpdReqDto draftSheetUpdReqDto) {
		log.debug("updateRouteSheet] - Try to update RouteS heet: '{}'", draftSheetUpdReqDto);
		Locale loc = LocaleContextHolder.getLocale();

		trimSpaces(draftSheetUpdReqDto);

		AuditResponse response = routeSheetUpdReqDtoAuditor.inspectUpdatedData(draftSheetUpdReqDto, null, loc);

		if (response.isInvalid()) {
			log.info("updateRouteSheet] - Route Sheet update is fail. Inform to the updater");
			FacilityDtoAuditExeption ex = new FacilityDtoAuditExeption(response);
			ex.setErrMsg(i18n.getTranslation(RestV1Msg.ROUTESHEET_UPDATE_FAIL, loc));
			ex.setErrMsgExt(i18n.getTranslation(RestV1Msg.ROUTESHEET_UPDATE_FAIL_EXT, loc));
			throw ex;
		}

		Optional<RouteSheetDto> routeSheetDtoO = draftRouteSheetServiceToRepo.fetchById(draftSheetUpdReqDto.getId());
		if (routeSheetDtoO.isPresent()) {
			RouteSheetDto routeSheetDto = routeSheetDtoO.get();
			if (!routeSheetDto.getDescription().equals(draftSheetUpdReqDto.getDescription())) {
				UserDto caller = userDtoMapper.getUserDto(
						((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
								.getUser());
				routeSheetDto.getHistoryEvents().add(HistoryEventDto.builder()
						.author(caller)
						.description("Description was changed from '"
								+ routeSheetDto.getDescription()
								+ "' to '"
								+ draftSheetUpdReqDto.getDescription() + "'")
						.build());
				routeSheetDto.setDescription(draftSheetUpdReqDto.getDescription());
				return draftRouteSheetServiceToRepo.save(routeSheetDto);
			}
		}

		return routeSheetDtoO;
	}

	protected void trimSpaces(DraftSheetUpdReqDto draftSheetUpdReqDto) {
		if (draftSheetUpdReqDto == null) {
			return;
		}
		draftSheetUpdReqDto.setDescription(StringUtils.trim(draftSheetUpdReqDto.getDescription()));
	}
}
