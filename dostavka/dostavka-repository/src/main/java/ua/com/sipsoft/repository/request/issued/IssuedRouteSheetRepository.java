package ua.com.sipsoft.repository.request.issued;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.dao.request.issued.IssuedRouteSheet;

/**
 * The Interface IssuedRouteSheetRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface IssuedRouteSheetRepository extends JpaRepository<IssuedRouteSheet, Long> {

}