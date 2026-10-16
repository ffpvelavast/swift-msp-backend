/** Data transfer object carrying TeamEventTypeDropDown data between the API layer and the service/client. */
package com.rex.commodis.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamEventTypeDropDownDto {

    private Long teamEventTypeId;
    private String displayText;
    private String description;
}