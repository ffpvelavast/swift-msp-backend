/** Spring Data JPA repository for RexUserRole entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.RexUserRole;
import com.rex.commodis.model.RexUserRoleId;

@Repository
public interface RexUserRoleRepository extends JpaRepository<RexUserRole, RexUserRoleId> {
}
