/** Spring Data JPA repository for InvoiceToTeamEvent entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.invoices;

import com.rex.commodis.model.invoices.InvoiceToTeamEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceToTeamEventRepository
        extends JpaRepository<InvoiceToTeamEvent, Long> {

    Optional<InvoiceToTeamEvent> findByInvoiceId(Long invoiceId);
}


