/** Spring Data JPA repository for InvoiceToCorporateEvent entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.invoices;

import com.rex.commodis.model.invoices.InvoiceToCorporateEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceToCorporateEventRepository
        extends JpaRepository<InvoiceToCorporateEvent, Long> {

    Optional<InvoiceToCorporateEvent> findByInvoiceId(Long invoiceId);
}


