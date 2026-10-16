/** Spring Data JPA repository for Filetypes entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.receipts.Filetypes;

@Repository
public interface FiletypesRepository extends JpaRepository<Filetypes, Short> {
}