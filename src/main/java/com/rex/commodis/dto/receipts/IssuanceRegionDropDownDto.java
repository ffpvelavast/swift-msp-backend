/** Data transfer object carrying IssuanceRegionDropDown data between the API layer and the service/client. */
package com.rex.commodis.dto.receipts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssuanceRegionDropDownDto {

    private Integer issuanceRegionId;
    private String regionCode;
    private String displayText;
    private String description;
}
