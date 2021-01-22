package ua.com.sipsoft.repository.common;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.user.User;

/**
 * The Interface FacilityRepository.
 */
@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

	/**
	 * Gets the by name.
	 *
	 * @param name     the name
	 * @param pageable the pageable
	 * @return the by name
	 */
	@Query("from Facility f " + "where LOWER ( f.name ) " + " like concat('%',:name,'%') ")
	Page<Facility> getByName(String name, Pageable pageable);

	@Query("from Facility f " + "where LOWER ( f.name ) " + " like concat('%',:name,'%') ")
	List<Facility> getByName(String name);

	@Query("select f from Facility as f where :user member of f.users")
	List<Facility> getAllOwnedByUser(User user);

	@Query("select f from Facility as f where :user member of f.users")
	Page<Facility> getAllOwnedByUser(User user, Pageable pageable);

	@Query("select f from Facility as f where :user member of f.users")
	List<Facility> getAllOwnedByUser(User user, Sort sort);

}