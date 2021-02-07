package ua.com.sipsoft.repository.common;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddr;

/**
 * The Interface FacilityAddrRepo.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface FacilityAddrRepo extends JpaRepository<FacilityAddr, Long> {

	/**
	 * Find by facility.
	 *
	 * @param facility the facility
	 * @return the list
	 */
	List<FacilityAddr> findByFacility(Facility facility);

	/**
	 * Gets the by facility id.
	 *
	 * @param id   the id
	 * @param sort the sort
	 * @return the by facility id
	 */
	@Query(" FROM FacilityAddr fa "
			+ " WHERE fa.facility.id = :facilityid ")
	List<FacilityAddr> getByFacilityId(@Param("facilityid") Long id, Sort sort);

	@Query(value = "select fa.* "
			+ "from facility_address as fa"
			+ "  left outer join facilities as f"
			+ "    on fa.facility_id = f.facility_id"
			+ "  left outer join users_facilities as uf"
			+ "    on fa.id = uf.facility_id "
			+ "where uf.user_id = :uid", nativeQuery = true)
	List<FacilityAddr> getdAllWithUserId(@Param("uid") Long facilityUserId);

}
