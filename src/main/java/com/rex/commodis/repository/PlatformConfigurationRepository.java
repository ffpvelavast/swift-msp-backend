/** Spring Data JPA repository for PlatformConfiguration entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.model.PlatformConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlatformConfigurationRepository
        extends JpaRepository<PlatformConfiguration, Long> {

    Optional<PlatformConfiguration> findByKeycloakRealm(String realm);

    @Query("SELECT pc FROM PlatformConfiguration pc WHERE pc.platform.id = :platformId AND CURRENT_DATE BETWEEN pc.activeFrom AND pc.activeThrough")
    Optional<PlatformConfiguration> findActiveByPlatformId(@Param("platformId") long platformId);
}
