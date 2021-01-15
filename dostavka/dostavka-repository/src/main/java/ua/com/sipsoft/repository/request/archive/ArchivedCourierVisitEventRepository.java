package ua.com.sipsoft.repository.request.archive;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.dao.request.archive.ArchivedCourierVisitEvent;

/**
 * The Interface ArchivedCourierVisitEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface ArchivedCourierVisitEventRepository extends JpaRepository<ArchivedCourierVisitEvent, Long> {

}