/** Spring Data JPA repository for PicklistMembersView entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rex.commodis.mapper.PicklistMemberRowMapper;
import com.rex.commodis.model.PicklistMemberRow;

import java.util.List;
import java.util.Objects;

@Repository
public class PicklistMembersViewRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PicklistMemberRowMapper rowMapper = new PicklistMemberRowMapper();

    public PicklistMembersViewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Fetch members for a specific picklist using the stored function.
     */
    public List<PicklistMemberRow> findByPicklistId(Long picklistId) {
        Objects.requireNonNull(picklistId, "picklistId must not be null");
        // Secure call to your stored function
        final String sql = "SELECT * FROM rexbase.picklist_members(?)";
        return jdbcTemplate.query(sql, rowMapper, picklistId);
    }
}
