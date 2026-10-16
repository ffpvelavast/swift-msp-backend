/** Data transfer object carrying ExpenseCategoryDropDown data between the API layer and the service/client. */
package com.rex.commodis.dto.receipts;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCategoryDropDownDto {

    private Long taxCategoryId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String displaytext;
    private String description;
}