/** Spring Data JPA repository for Employer entities; extends JpaRepository and adds custom query methods. */

package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.Employer;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
}
