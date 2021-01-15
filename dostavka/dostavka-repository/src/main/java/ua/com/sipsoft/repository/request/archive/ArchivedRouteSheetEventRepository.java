package ua.com.sipsoft.repository.request.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.dao.request.archive.ArchivedRouteSheetEvent;

/**
 * The Interface ArchivedRouteSheetEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface ArchivedRouteSheetEventRepository extends JpaRepository<ArchivedRouteSheetEvent, Long> {

}