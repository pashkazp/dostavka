package ua.com.sipsoft.ui.model.response.request.daft;

import java.io.Serializable;

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
public class DraftSheetCardResponse implements Serializable {

	private static final long serialVersionUID = -270696402784834928L;

	private Long id;

	private String author;

	private String description;

	private String creationDate;

}
