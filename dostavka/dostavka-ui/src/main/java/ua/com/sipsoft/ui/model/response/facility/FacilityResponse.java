package ua.com.sipsoft.ui.model.response.facility;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

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
public class FacilityResponse implements Serializable {

	private static final long serialVersionUID = -2622437157667492847L;

	private Long id;

	@NotEmpty
	private String name;

}
