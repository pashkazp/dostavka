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
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.request.CourierRequestDto;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.util.security.Role;

/**
 * The Class CourierRequestFilter.
 *
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CourierRequestFilter implements EntityFilter<CourierRequestDto> {

	@Builder.Default
	private User author = null;

	@Builder.Default
	private LocalDateTime fromLocalDateTime = null;

	@Builder.Default
	private LocalDateTime toLocalDateTime = null;

	@Builder.Default
	private String description = null;

	@Builder.Default
	private Long fromPointId = null;

	@Builder.Default
	private Long toPointId = null;

	@Builder.Default
	private User caller = null;

	@Override
	public boolean isPass(CourierRequestDto entity) {
		if (entity == null) {
			return false;
		}
		if (!containsIgnoreCase(entity.getDescription(), defaultString(description))) {
			return false;
		}
		if (author != null && entity.getAuthor().getId() != this.author.getId()) {
			return false;
		}
		if (caller != null) {
			if (caller.getHighesRole().ordinal() == Role.ROLE_CLIENT.ordinal()) {
				if (entity.getAuthor().getId() != caller.getId()) {
					return false;
				}
			}
		}
		return true;
	}

}