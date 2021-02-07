package ua.com.sipsoft.service.dto.facility;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class FacilityAddrRegReqDto implements Serializable {

	private static final long serialVersionUID = -7000975428120207482L;

	@Builder.Default
	private String addressesAlias = "";

	@Builder.Default
	private String address = "";

	@Builder.Default
	private boolean defaultAddress = false;

	private String lat;

	private String lng;

	public boolean isEmpty() {
		if (StringUtils.isNotEmpty(addressesAlias)
				|| StringUtils.isNotEmpty(address)
				|| StringUtils.isNotEmpty(lat)
				|| StringUtils.isNotEmpty(lng)) {
			return false;
		}
		return true;
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

}
