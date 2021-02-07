package ua.com.sipsoft.service.dto.facility;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

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
public class FacilityDto implements Serializable {

	private static final long serialVersionUID = -4966655948589711021L;
	private Long id;

	private Long version = 0L;

	@NotEmpty
	private String name = "";

	private Set<UserDto> users = new HashSet<>();

	// @JsonBackReference
	// @JsonIgnoreProperties("facility")
	private Set<FacilityAddrDto> facilityAddresses = new HashSet<>();

	public void addFacilityAddress(FacilityAddrDto facilityAddress) {
		log.info("Try to add facility addresses");
		if (facilityAddress != null && facilityAddresses != null) {
			facilityAddresses.add(facilityAddress);
		}
	}

	public void delFacilityAddress(FacilityAddrDto facilityAddress) {
		log.info("Try to remove fasility addresses");
		if (facilityAddress != null && facilityAddresses != null) {
			facilityAddresses.remove(facilityAddress);
		}
	}
}
