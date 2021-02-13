package ua.com.sipsoft.service.request.draft;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.sipsoft.dao.request.draft.DraftRouteSheet;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.util.EntityFilter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DraftSheetFilter implements EntityFilter<DraftRouteSheet> {

	private User author;

	private LocalDateTime creationDate;

	private String description;

	@Override
	public boolean isPass(DraftRouteSheet entity) {
		if (entity == null) {
			return false;
		}
		if (!containsIgnoreCase(entity.getDescription(), defaultString(description))) {
			return false;
		}
		if (author != null && entity.getAuthor().getId() != author.getId()) {
			return false;
		}
		if (creationDate != null && entity.getCreationDate() != null
				&& !creationDate.isEqual(entity.getCreationDate())) {
			return false;
		}
		return true;
	}

}
