package ua.com.sipsoft.service.dto.request;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.user.UserDto;

@ToString()
@Getter
@Setter
@NoArgsConstructor
@Slf4j
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteSheetDto implements Serializable {

	private static final long serialVersionUID = -7326508658882857283L;

	private Long id;

	private Long version = 0L;

	private UserDto author;

	private UserDto executor;

	private LocalDateTime creationDate = LocalDateTime.now();

	private String description = "";

	private Set<HistoryEventDto> historyEvents;

	private Set<CourierRequestDto> requests;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RouteSheetDto)) {
			return false;
		}
		RouteSheetDto other = (RouteSheetDto) obj;
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
