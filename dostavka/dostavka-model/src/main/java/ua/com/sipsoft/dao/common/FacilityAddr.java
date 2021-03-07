package ua.com.sipsoft.dao.common;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Simple JavaBeen domain object that represents address of {@link Facility}.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "facility_address")
@Slf4j
@Builder
@AllArgsConstructor
public class FacilityAddr implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4398276671459742834L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The version. */
	@Version
	@Column(nullable = false)
	@Builder.Default
	private Long version = 0L;

	/** The addresses alias. */
	@Column(name = "address_alias", length = 100, nullable = false)
	@Builder.Default
	private String addressesAlias = "";

	/** The address. */
	@Column(name = "address", length = 255, nullable = false)
	@Builder.Default
	private String address = "";

	/** The default address. */
	@Column(name = "default_address", columnDefinition = "boolean default false", nullable = false)
	@Builder.Default
	private boolean defaultAddress = false;

//	/** The geo coordinates. */
//	@Column(name = "geocoordinates", length = 50, nullable = false)
//	private String geoCoordinates = "";

	@Column(name = "lat", nullable = true)
	private Double lat;

	@Column(name = "lng", nullable = true)
	private Double lng;

	/** The facility. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "facility_id")
	@Builder.Default
	private Facility facility = new Facility();

	/**
	 * Instantiates a new facility address.
	 *
	 * @param addressAlias the address alias
	 * @param address      the address
	 */
	public FacilityAddr(String addressAlias, String address) {
		super();
		log.info("Instantiates a new facility address. Alias '{}' address '{}'", addressAlias, address);
		this.addressesAlias = addressAlias;
		this.address = address;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof FacilityAddr)) {
			return false;
		}
		FacilityAddr other = (FacilityAddr) obj;
		if (this.id == null && other.id == null) {
			return false;
		}
		return Objects.equals(id, other.id);
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FacilityAddr [id=").append(id).append(", version=").append(version)
				.append(", addressesAlias=").append(addressesAlias).append(", address=").append(address)
				.append(", defaultAddress=").append(defaultAddress).append(", lat=").append(lat).append(", lng=")
				.append(lng).append(", facility_id=").append(facility.getId()).append("]");
		return builder.toString();
	}
}