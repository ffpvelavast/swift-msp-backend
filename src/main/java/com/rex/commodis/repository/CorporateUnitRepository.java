/** Spring Data JPA repository for CorporateUnit entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.model.CorporateUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorporateUnitRepository extends JpaRepository<CorporateUnit, Long> {

    List<CorporateUnit> findByCorporateDivisionIdOrderBySortIdAsc(Long corporateDivisionId);
}
