/** Spring Data JPA repository for CorporateEventAdmin entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.events.CorporateEventAdmin.CorporateEventAdmin;
import com.rex.commodis.model.events.CorporateEventAdmin.CorporateEventAdminId;

@Repository
public interface CorporateEventAdminRepository extends JpaRepository<CorporateEventAdmin, CorporateEventAdminId> {
}
