package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.request.DraftSheetUpdReqDto;
import ua.com.sipsoft.ui.model.request.request.draft.DraftSheetUpdReq;

@Mapper(componentModel = "spring")
@Component
public interface ToDraftSheetUpdDtoMapper {

	DraftSheetUpdReqDto fromDraftSheetUpdReq(DraftSheetUpdReq draftSheetUpdReq);
}
