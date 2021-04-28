package ua.com.sipsoft.ui.model.response.request.draft;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DraftReqsCardResponse implements Serializable {

	private static final long serialVersionUID = -5444702374367279740L;

	private Long id;

	private String author;

	private String description;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
	private LocalDateTime creationDate;

	private Long fromId;

	private String fromAddr;

	private String fromFacility;

	private long toId;

	private String toAddr;

	private String toFacility;

}
