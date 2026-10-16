package com.rex.commodis.repository;

import com.rex.commodis.model.TranslationClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TranslationClientRepository extends JpaRepository<TranslationClient, Long> {

    @Query("SELECT t FROM TranslationClient t WHERE t.platformId = :platformId ORDER BY t.component, t.key")
    List<TranslationClient> findByPlatformId(@Param("platformId") Long platformId);

    @Query("SELECT t FROM TranslationClient t WHERE t.platformId = :platformId AND t.component = :component ORDER BY t.key")
    List<TranslationClient> findByPlatformIdAndComponent(@Param("platformId") Long platformId,
                                                          @Param("component") String component);
}
