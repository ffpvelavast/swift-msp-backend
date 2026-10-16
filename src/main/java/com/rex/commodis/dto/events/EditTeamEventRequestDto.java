package com.rex.commodis.dto.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditTeamEventRequestDto {

    @FutureOrPresent(message = "bookable_through cannot be in the past")
    @JsonProperty("bookable_through")
    private LocalDate bookableThrough;

    @JsonProperty("teameventtype_id")
    private Long teameventtypeId;

    @JsonProperty("picklist_id")
    private Long picklistId;
}