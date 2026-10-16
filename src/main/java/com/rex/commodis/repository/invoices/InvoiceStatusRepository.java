/** Spring Data JPA repository for InvoiceStatus entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.invoices;

import com.rex.commodis.model.enums.InvoiceStatusEnum;
import com.rex.commodis.model.invoices.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceStatusRepository extends JpaRepository<InvoiceStatus, Long> {

    Optional<InvoiceStatus> findByStatus(InvoiceStatusEnum status);
}


