package ua.com.sipsoft.repository.request.draft;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.dao.request.draft.DraftRouteSheetEvent;

/**
 * The Interface DraftRouteSheeEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface DraftRouteSheeEventRepository extends JpaRepository<DraftRouteSheetEvent, Long> {

}