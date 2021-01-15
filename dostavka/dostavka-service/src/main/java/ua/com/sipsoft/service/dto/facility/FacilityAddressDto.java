package ua.com.sipsoft.service.dto.facility;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString()
@Getter
@Setter
@NoArgsConstructor
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacilityAddressDto implements Serializable {

	private static final long serialVersionUID = -2195070643307661176L;
	/** The id. */
	private Long id;

	private Long version = 0L;

	private String addressesAlias = "";

	@NotEmpty
	private String address = "";

	private boolean defaultAddress = false;

//	private String geoCoordinates = "";

	private Double lat;

	private Double lng;

}
