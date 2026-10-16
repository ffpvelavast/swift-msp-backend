/** Spring Data JPA repository for DeputyAssignment entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.DeputyAssignment;

@Repository
public interface DeputyAssignmentRepository extends JpaRepository<DeputyAssignment, Long> {
}
