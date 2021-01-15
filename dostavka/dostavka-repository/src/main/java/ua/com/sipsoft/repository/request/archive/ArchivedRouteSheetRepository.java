package ua.com.sipsoft.repository.request.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.dao.request.archive.ArchivedRouteSheet;

/**
 * The Interface ArchivedRouteSheetRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface ArchivedRouteSheetRepository extends JpaRepository<ArchivedRouteSheet, Long> {

}