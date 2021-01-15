package ua.com.sipsoft.service.dto.request;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.facility.FacilityAddressDto;
import ua.com.sipsoft.service.dto.user.UserDto;
import ua.com.sipsoft.util.CourierVisitState;

@ToString()
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class CourierRequestDto implements Serializable {

	private static final long serialVersionUID = 2232254498469954921L;

	private Long id;

	private Long version = 0L;

	private UserDto author;

	private LocalDateTime creationDate = LocalDateTime.now();

	private FacilityAddressDto fromPoint;

	private FacilityAddressDto toPoint;

	private String description = "";

	private Set<HistoryEventDto> historyEvents;

	private CourierVisitState state = CourierVisitState.NEW;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CourierRequestDto)) {
			return false;
		}
		CourierRequestDto other = (CourierRequestDto) obj;
		if (this.id == null && other.id == null) {
			return false;
		}
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
