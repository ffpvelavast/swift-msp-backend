/** Spring Data JPA repository for InvoiceError entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.invoices;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rex.commodis.model.invoices.InvoiceError;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceErrorRepository extends JpaRepository<InvoiceError, Long> {
}
