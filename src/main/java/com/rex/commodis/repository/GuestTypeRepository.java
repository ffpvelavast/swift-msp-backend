/** Spring Data JPA repository for GuestType entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.model.GuestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestTypeRepository extends JpaRepository<GuestType, Long> {
    Optional<GuestType> findFirstByGuestEnum(String guestEnum);
}