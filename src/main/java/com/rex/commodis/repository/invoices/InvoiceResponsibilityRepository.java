/** Spring Data JPA repository for InvoiceResponsibility entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.invoices;

import com.rex.commodis.model.invoices.InvoiceResponsibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceResponsibilityRepository extends JpaRepository<InvoiceResponsibility, Long> {

    boolean existsByInvoiceIdAndDelegateIdAndIsActiveTrue(
            Long invoiceId,
            Long rexUserId
    );

    Optional<InvoiceResponsibility> findByInvoiceIdAndDelegateIdAndIsActiveTrue(
            Long invoiceId,
            Long delegateId
    );

}


