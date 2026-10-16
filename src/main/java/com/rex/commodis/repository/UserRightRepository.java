/** Spring Data JPA repository for UserRight entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.UserRight;

@Repository
public interface UserRightRepository extends JpaRepository<UserRight, Long> {
}
