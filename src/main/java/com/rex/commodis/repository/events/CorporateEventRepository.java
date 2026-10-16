/** Spring Data JPA repository for CorporateEvent entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.events.CorporateEvent;

@Repository
public interface CorporateEventRepository extends JpaRepository<CorporateEvent, Long> {
}
