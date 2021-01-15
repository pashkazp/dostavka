package ua.com.sipsoft.repository.request.issued;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.dao.request.issued.IssuedRouteSheetEvent;

/**
 * The Interface IssuedRouteSheetEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface IssuedRouteSheetEventRepository extends JpaRepository<IssuedRouteSheetEvent, Long> {

}