/** Spring Data JPA repository for CorporateDivision entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.model.CorporateDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorporateDivisionRepository extends JpaRepository<CorporateDivision, Long> {

    List<CorporateDivision> findByPlatformIdOrderBySortIdAsc(Long platformId);
}
