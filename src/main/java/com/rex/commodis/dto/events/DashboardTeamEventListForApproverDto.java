package com.rex.commodis.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardTeamEventListForApproverDto {
    private  long eventId;
    private String eventName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventDate;

    private String status;
}