/** Spring Data JPA repository for Picklist entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.model.Picklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 *
 * Repository for calling DB function rexbase.picklist_members
 */
public interface PicklistRepository extends JpaRepository<Picklist, Long> {

  @Query(value = "SELECT * FROM rexbase.picklist_members(:picklistId, :rexUserId)", nativeQuery = true)
  List<Object[]> findPicklistMembersRaw(@Param("picklistId") Long picklistId, @Param("rexUserId") Long rexUserId);

  List<Picklist> findAllByRexUserIdOrderBySortIdAsc(Long rexUserId);

  @Query(value = """
          SELECT
            (SELECT COUNT(*) FROM rexbase.picklist_to_beneficiary WHERE picklist_id = :id) +
            (SELECT COUNT(*) FROM rexbase.picklist_to_guest WHERE picklist_id = :id)
      """, nativeQuery = true)
  Long countMembers(@Param("id") Long picklistId);

  Optional<Picklist> findByRexUserIdAndDisplayName(Long rexUserId, String displayName);

  Optional<Picklist> findByIdAndRexUserId(Long id, Long rexUserId);
}
