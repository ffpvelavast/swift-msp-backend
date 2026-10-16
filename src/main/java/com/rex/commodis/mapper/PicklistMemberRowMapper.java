/** MapStruct mapper converting between PicklistMemberRow entities and DTOs. */
package com.rex.commodis.mapper;

import org.springframework.jdbc.core.RowMapper;
import com.rex.commodis.model.PicklistMemberRow;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PicklistMemberRowMapper implements RowMapper<PicklistMemberRow> {

    @Override
    public PicklistMemberRow mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PicklistMemberRow.builder()
                .memberType(rs.getString("member_type"))
                .memberId(rs.getObject("member_id", Long.class))
                .listname(rs.getString("listname"))
                .organization(rs.getString("organization"))
                .identityTag(rs.getString("identity_tag"))
                .orgunit(rs.getString("orgunit"))
                .costcenter(rs.getString("costcenter"))
                .build();
    }
}
