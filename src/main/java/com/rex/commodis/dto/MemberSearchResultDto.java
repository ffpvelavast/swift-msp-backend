/** Data transfer object carrying MemberSearchResult data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchResultDto {
    private String memberType;
    private Long memberId;
    private String listname;
    private String companyname;
    private String identitytag;
    private Boolean isFavorite;
}
