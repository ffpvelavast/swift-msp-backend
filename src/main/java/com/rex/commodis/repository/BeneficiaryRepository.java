/** Spring Data JPA repository for Beneficiary entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.model.Beneficiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long>, JpaSpecificationExecutor<Beneficiary> {
    java.util.Optional<Beneficiary> findByBeneficiaryId(java.util.UUID beneficiaryId);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(b) FROM Beneficiary b WHERE b.platform.id = :platformId")
    long countByPlatformId(@org.springframework.data.repository.query.Param("platformId") Long platformId);

    @Override
    @NonNull
    Page<Beneficiary> findAll(@Nullable Specification<Beneficiary> specification, @NonNull Pageable pageable);

    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM rexbase.select_beneficiary(:platformId)", nativeQuery = true)
    java.util.List<Object[]> selectBeneficiariesByPlatform(@org.springframework.data.repository.query.Param("platformId") Long platformId);
}
