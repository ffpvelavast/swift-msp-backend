/** Spring Data JPA repository for Guest entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    /**
     * Using your production stored function for listing guests.
     */
    @Query(value = "SELECT * FROM rexbase.select_guests(:platformId, :rexUserId, :locale)", nativeQuery = true)
    List<GuestProjection> findGuestsRaw(
            @Param("platformId") Long platformId,
            @Param("rexUserId") Long rexUserId,
            @Param("locale") String locale);

    @Query(value = "SELECT * FROM rexbase.select_guests(:platformId, :rexUserId, :locale) LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<GuestProjection> findGuestsPaged(
            @Param("platformId") Long platformId,
            @Param("rexUserId") Long rexUserId,
            @Param("locale") String locale,
            @Param("limit") int limit,
            @Param("offset") int offset);

    /**
     * Used to prevent creating exact duplicate guests on the same platform.
     */
    java.util.Optional<Guest> findFirstByPlatformIdAndFirstnameIgnoreCaseAndLastnameIgnoreCaseAndOrganizationIgnoreCase(
            Long platformId, String firstname, String lastname, String organization);

    /**
     * Professional Projection to map database columns by name instead of index.
     */
    interface GuestProjection {
        Long getGuest_id(); // Matches 'guest_id' column
        Boolean getOrganisationwide_guest();
        Object getListname();
        Object getOrganization();
        Object getGuest_enum();
        Object getGuest_type();
    }
}