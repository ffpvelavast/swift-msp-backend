/** Data transfer object carrying TeamEventGetList data between the API layer and the service/client. */
package com.rex.commodis.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TeamEventGetListDto {
    private Long teamEventId;
    private String status;
    private LocalDate eventDate;
    private String eventTitle;
    private String submittedBy;
    private String projectNumber;
    private BigDecimal totalAmount;
}
