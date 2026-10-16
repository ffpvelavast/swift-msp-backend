/** Spring Data JPA repository for BeneficiaryOrgView entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.mapper.BeneficiaryOrgRowMapper;
import com.rex.commodis.model.BeneficiaryOrgRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BeneficiaryOrgViewRepository {

    private final JdbcTemplate jdbcTemplate;
    private final BeneficiaryOrgRowMapper internalMapper = new BeneficiaryOrgRowMapper();

    public BeneficiaryOrgViewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BeneficiaryOrgRow> findInternal(Long platformId, Long rexUserId) {
        final String sql = "SELECT * FROM rexbase.select_beneficiary(?, ?)";
        return jdbcTemplate.query(sql, internalMapper, platformId, rexUserId);
    }

    public List<BeneficiaryOrgRow> findPaged(Long platformId, Long rexUserId, int limit, int offset) {
        final String sql = "SELECT * FROM rexbase.select_beneficiary(?, ?) LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, internalMapper, platformId, rexUserId, limit, offset);
    }

    /**
     * Fetch a specific beneficiary using its ID and Platform safety with favorite
     * status.
     */
    public List<BeneficiaryOrgRow> findByBeneficiaryId(Long beneficiaryId, Long platformId, Long rexUserId) {
        final String sql = "SELECT * FROM rexbase.select_beneficiary(?, ?) WHERE beneficiary_id = ?";
        return jdbcTemplate.query(sql, internalMapper, platformId, rexUserId, beneficiaryId);
    }
}
