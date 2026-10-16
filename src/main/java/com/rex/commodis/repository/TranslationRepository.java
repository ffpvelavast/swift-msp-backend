/** Spring Data JPA repository for Translation entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rex.commodis.model.Translation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TranslationRepository extends JpaRepository<Translation, Long> {
    
    // ✅ Get ALL keys for a component
    @Query("SELECT t FROM Translation t WHERE t.component = :component")
    List<Translation> findByComponent(@Param("component") String component);
    
    // ✅ Get single key for component
    Optional<Translation> findByComponentAndKey(String component, String key);
    
    // ✅ Get MAX(updated_at) for cache versioning
    @Query("SELECT MAX(t.updatedAt) FROM Translation t")
    Optional<LocalDateTime> getMaxUpdatedAt();

    // ✅ Export — native query avoids unmapped columns (e.g. platform_id absent in older schemas)
    @Query(value = "SELECT component, key, translations FROM rexbase.translations ORDER BY component, key", nativeQuery = true)
    List<Object[]> findAllForExport();

}
