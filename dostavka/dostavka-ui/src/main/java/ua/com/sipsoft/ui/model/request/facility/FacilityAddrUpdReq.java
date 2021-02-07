package ua.com.sipsoft.ui.model.request.facility;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class FacilityAddrUpdReq implements Serializable {

	private static final long serialVersionUID = 124090882967902928L;

	private String addressesAlias;

	private String address;

	private boolean defaultAddress;

	private String lat;

	private String lng;

}
