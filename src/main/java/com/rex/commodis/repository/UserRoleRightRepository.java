/** Spring Data JPA repository for UserRoleRight entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.UserRoleRight;
import com.rex.commodis.model.UserRoleRightId;

@Repository
public interface UserRoleRightRepository extends JpaRepository<UserRoleRight, UserRoleRightId> {
}
