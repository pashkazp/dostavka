package ua.com.sipsoft.service.dto.request;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@AllArgsConstructor
@Slf4j
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryEventDto implements Serializable {
	private static final long serialVersionUID = 5680689351311416297L;

	private Long id;

	@Builder.Default
	private Long version = 0L;

	@Builder.Default
	private LocalDateTime creationDate = LocalDateTime.now();

	@Builder.Default
	private String description = "";

	private UserDto author;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof HistoryEventDto)) {
			return false;
		}
		HistoryEventDto other = (HistoryEventDto) obj;
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
