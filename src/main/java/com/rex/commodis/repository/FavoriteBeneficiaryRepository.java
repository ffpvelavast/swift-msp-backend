/** Spring Data JPA repository for FavoriteBeneficiary entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.model.FavoriteBeneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteBeneficiaryRepository extends JpaRepository<FavoriteBeneficiary, Long> {

    List<FavoriteBeneficiary> findAllByRexUserIdOrderBySortIdAsc(Long rexUserId);

    void deleteByRexUserIdAndBeneficiaryId(Long rexUserId, Long beneficiaryId);

    @Modifying
    @Query(value = "INSERT INTO rexbase.beneficiary_favorites (rexuser_id, beneficiary_id, sortid) VALUES (:rexUserId, :beneficiaryId, 0)", nativeQuery = true)
    void insertOne(@Param("rexUserId") Long rexUserId, @Param("beneficiaryId") Long beneficiaryId);
}