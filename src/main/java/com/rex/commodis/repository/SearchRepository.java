/** Spring Data JPA repository for Search entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import com.rex.commodis.dto.MemberSearchResultDto;
import com.rex.commodis.mapper.MemberSearchResultMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SearchRepository {

    private final JdbcTemplate jdbcTemplate;
    private final MemberSearchResultMapper mapper = new MemberSearchResultMapper();

    public SearchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MemberSearchResultDto> searchMembers(String searchText, Long rexUserId, Long platformId, int limit) {
        final String sql = "SELECT * FROM rexbase.search_members(?, ?, ?, ?)";
        return jdbcTemplate.query(sql, mapper, searchText, rexUserId, platformId, limit);
    }
}
