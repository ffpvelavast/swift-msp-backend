/** Data transfer object carrying BeneficiaryNames data between the API layer and the service/client. */
package com.rex.commodis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryNamesDto {

    @JsonProperty("fullnames")
    private List<String> fullnames;
}