/** Spring Data JPA repository for ProjectNumber entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.mapper.ProjectNumberSearchProjection;
import com.rex.commodis.model.ProjectNumber;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectNumberRepository
        extends JpaRepository<ProjectNumber, Long>, JpaSpecificationExecutor<ProjectNumber> {
    Optional<ProjectNumber> findByProjectNumberId(UUID projectId);

    Optional<ProjectNumber> findByProjectNumber(String projectNumber);

    Optional<ProjectNumber> findByPlatformIdAndProjectNumber(Long platformId, String projectNumber);

    Optional<ProjectNumber> findByPlatformIdAndProjectNumberId(Long platformId, UUID projectNumberId);

    Page<ProjectNumber> findByPlatformId(Long platformId, Pageable pageable);

    Page<ProjectNumber> findByPlatformIdAndProjectNumber(Long platformId, String projectNumber, Pageable pageable);

    @Query(value = """
            SELECT 
                projectnumber_id,
                projectnumber,
                projecttitle::text,
                projectsubtitle::text
            FROM rexbase.select_projectnumber(:platformId, :searchText, :limit)
        """, nativeQuery = true)
    List<ProjectNumberSearchProjection> searchProjectNumbers(
            @Param("platformId") Long platformId,
            @Param("searchText") String searchText,
            @Param("limit") Integer limit
    );
}
