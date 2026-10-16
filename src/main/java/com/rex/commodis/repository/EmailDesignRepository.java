/** Spring Data JPA repository for EmailDesign entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.model.EmailDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailDesignRepository extends JpaRepository<EmailDesign, Long> {

    Optional<EmailDesign> findFirstByOrderByIdAsc();
}
