/** MapStruct mapper converting between MemberSearchResult entities and DTOs. */
package com.rex.commodis.mapper;

import org.springframework.jdbc.core.RowMapper;
import com.rex.commodis.dto.MemberSearchResultDto;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberSearchResultMapper implements RowMapper<MemberSearchResultDto> {

    @Override
    public MemberSearchResultDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MemberSearchResultDto.builder()
                .memberType(rs.getString("member_type"))
                .memberId(rs.getLong("member_id"))
                .listname(rs.getString("listname"))
                .companyname(rs.getString("companyname"))
                .identitytag(rs.getString("identitytag"))
                .isFavorite(rs.getBoolean("isfavorite"))
                .build();
    }
}
