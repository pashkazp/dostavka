package ua.com.sipsoft.repository.request.issued;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.dao.request.issued.CourierVisitEvent;

/**
 * The Interface CourierVisitEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface CourierVisitEventRepository extends JpaRepository<CourierVisitEvent, Long> {

}