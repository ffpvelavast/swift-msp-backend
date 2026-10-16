/** MapStruct mapper converting between BeneficiaryOrgRow entities and DTOs. */
package com.rex.commodis.mapper;

import org.springframework.jdbc.core.RowMapper;
import com.rex.commodis.model.BeneficiaryOrgRow;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BeneficiaryOrgRowMapper implements RowMapper<BeneficiaryOrgRow> {

    @Override
    public BeneficiaryOrgRow mapRow(ResultSet rs, int rowNum) throws SQLException {
        return BeneficiaryOrgRow.builder()
                .beneficiaryId(rs.getObject("beneficiary_id", Long.class))
                .listname(rs.getString("listname"))
                .fullname(rs.getString("fullname"))
                .identity_tag(rs.getString("identity_tag"))
                .employer(rs.getString("employer"))
                .orgunit(rs.getString("orgunit"))
                .costcenter(rs.getString("costcenter"))
                .isFavorite(rs.getBoolean("is_favorite"))
                .build();
    }
}
