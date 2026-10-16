/** Spring Data JPA repository for RexUserRights entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.RexUserRights;
import com.rex.commodis.model.RexUserRightsId;

@Repository
public interface RexUserRightsRepository extends JpaRepository<RexUserRights, RexUserRightsId> {
}
