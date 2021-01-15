package ua.com.sipsoft.repository.request.draft;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.sipsoft.dao.request.draft.CourierRequestEvent;

/**
 * The Interface CourierRequestEventRepository.
 *
 * @author Pavlo Degtyaryev
 */
@Repository
public interface CourierRequestEventRepository extends JpaRepository<CourierRequestEvent, Long> {

}