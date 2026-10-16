/** Spring Data JPA repository for PicklistToBeneficiary entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.PicklistToBeneficiary;
import com.rex.commodis.model.PicklistToBeneficiaryId;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface PicklistToBeneficiaryRepository
        extends JpaRepository<PicklistToBeneficiary, PicklistToBeneficiaryId> {

    void deleteByPicklistId(Long picklistId);

    void deleteByPicklistIdAndBeneficiaryId(Long picklistId, Long beneficiaryId);

    boolean existsByPicklistIdAndBeneficiaryId(Long picklistId, Long beneficiaryId);

    default void insertOne(Long picklistId, Long beneficiaryId) {
        save(PicklistToBeneficiary.builder().picklistId(picklistId).beneficiaryId(beneficiaryId).build());
    }
}
