package com.rex.commodis.dto.events;

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
public class MySubmittedTeamEventGetListDto {

    private Long teamEventId;

    private String status;

    private LocalDate eventDate;

    private String eventTitle;

    private String projectNumber;

    private BigDecimal totalAmount;
}