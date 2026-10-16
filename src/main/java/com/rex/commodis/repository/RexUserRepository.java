/** Spring Data JPA repository for RexUser entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.model.RexUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RexUserRepository extends JpaRepository<RexUser, Long> {
    // Find multiple users by a list of emails
    @Query(value = """
        SELECT * FROM rexbase.rexuser
        WHERE emailaddress = ANY(CAST(:emails AS citext[]))
        AND platform_id = :platformId
        AND active = true
    """, nativeQuery = true)
    List<RexUser> findRexUsersByEmailIdsAndPlatformId(
            @Param("emails") String[] emails,
            @Param("platformId") Long platformId
    );

    Optional<RexUser> findById(Long id);

    Optional<RexUser> findByIdAndActiveTrue(Long id);

    Optional<RexUser> findByEmailIgnoreCaseAndActiveTrue(String email);

    Optional<RexUser> findByEmailIgnoreCaseAndPlatformIdAndActiveTrue(String email, Long platformId);

    Optional<RexUser> findByKeycloakId(java.util.UUID keycloakId);

    Optional<RexUser> findByListname(String listName);

    List<RexUser> findByPlatformIdAndActiveTrue(Long platformId);

    @Query(value = "SELECT rexbase.fn_login_user_profile(CAST(:email AS TEXT))", nativeQuery = true)
    String getUserProfileJson(@Param("email") String email);

}
