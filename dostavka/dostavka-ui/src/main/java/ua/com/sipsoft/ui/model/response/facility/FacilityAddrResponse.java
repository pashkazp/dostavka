package ua.com.sipsoft.ui.model.response.facility;

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
public class FacilityAddrResponse implements Serializable {

	private static final long serialVersionUID = 6203685562206948511L;

	private Long id;

	private String addressesAlias = "";

	private String address = "";

	private boolean defaultAddress = false;

	private String lat;

	private String lng;
}
