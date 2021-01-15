package ua.com.sipsoft.service.util.audit;

import org.apache.commons.validator.routines.DoubleValidator;

public interface LatLngAuditor {

	default boolean isLatitudeAgreed(final Double coordinate, boolean isNullAccepted) {

		// if coordinate is null: return true if null accepted or false if null not
		// accepted.
		if (coordinate == null) {
			return isNullAccepted;
		}

		return DoubleValidator.getInstance().isInRange(coordinate, -90d, 90d);
	}

	default boolean isLongitudeAgreed(final Double coordinate, boolean isNullAccepted) {

		// if coordinate is null: return true if null accepted or false if null not
		// accepted.
		if (coordinate == null) {
			return isNullAccepted;
		}

		return DoubleValidator.getInstance().isInRange(coordinate, -180d, 180d);
	}

}