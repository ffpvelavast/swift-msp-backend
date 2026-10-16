/** Spring Data JPA repository for EmailAccount entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.model.EmailAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAccountRepository extends JpaRepository<EmailAccount, Long> {

    Optional<EmailAccount> findFirstByOrderByIdAsc();
}
