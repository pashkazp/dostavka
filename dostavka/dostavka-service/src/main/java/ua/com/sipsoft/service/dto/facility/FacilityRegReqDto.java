package ua.com.sipsoft.service.dto.facility;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class FacilityRegReqDto implements Serializable {

	private static final long serialVersionUID = -6621774694796773948L;

	@NotEmpty
	private String name;

	private FacilityAddrRegReqDto facilityAddress;

}
