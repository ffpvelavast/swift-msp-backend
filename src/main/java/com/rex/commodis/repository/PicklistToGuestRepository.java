/** Spring Data JPA repository for PicklistToGuest entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.PicklistToGuest;
import com.rex.commodis.model.PicklistToGuestId;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface PicklistToGuestRepository
        extends JpaRepository<PicklistToGuest, PicklistToGuestId> {

    void deleteByPicklistId(Long picklistId);

    List<PicklistToGuest> findByPicklistId(Long picklistId);

    List<PicklistToGuest> findByGuestId(Long guestId);
}
