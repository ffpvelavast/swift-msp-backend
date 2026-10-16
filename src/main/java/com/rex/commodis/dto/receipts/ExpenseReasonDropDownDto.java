/** Data transfer object carrying ExpenseReasonDropDown data between the API layer and the service/client. */
package com.rex.commodis.dto.receipts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseReasonDropDownDto {

    private Long taxReasonId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String displaytext;

    private String description;
}