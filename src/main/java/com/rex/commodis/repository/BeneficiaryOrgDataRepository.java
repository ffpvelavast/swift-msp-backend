/** Spring Data JPA repository for BeneficiaryOrgData entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.BeneficiaryOrgData;

@Repository
public interface BeneficiaryOrgDataRepository extends JpaRepository<BeneficiaryOrgData, Long> {
}
