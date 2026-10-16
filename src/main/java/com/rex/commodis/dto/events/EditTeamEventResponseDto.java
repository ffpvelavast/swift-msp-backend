package com.rex.commodis.dto.events;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditTeamEventResponseDto {

    private Long teamEventId;

    private LocalDate bookableThrough;

    private Long teameventtypeId;

    private Long picklistId;
}
