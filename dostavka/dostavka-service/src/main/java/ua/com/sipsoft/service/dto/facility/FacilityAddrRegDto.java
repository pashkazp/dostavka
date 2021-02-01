package ua.com.sipsoft.service.dto.facility;

import java.io.Serializable;

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
public class FacilityAddrRegDto implements Serializable {

	private static final long serialVersionUID = -7000975428120207482L;

	private String addressesAlias = "";

	private String address = "";

	private boolean defaultAddress = false;

	private String lat;

	private String lng;

}
