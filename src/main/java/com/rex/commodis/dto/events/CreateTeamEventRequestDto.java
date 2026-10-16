/** Request DTO for the CreateTeamEvent operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamEventRequestDto {

    @NotBlank(message = "event_title is required")
    @JsonProperty("event_title")
    private String eventTitle;

    @NotNull(message = "event_date is required")
    @JsonProperty("event_date")
    private LocalDate eventDate;

    @NotNull(message = "projectnumber_id is required")
    @JsonProperty("projectnumber_id")
    private Long projectNumberId;

    @NotNull(message = "teameventtype_id is required")
    @JsonProperty("teameventtype_id")
    private Long teamEventTypeId;

    @NotNull(message = "bookable_through is required")
    @FutureOrPresent(message = "bookable_through cannot be in the past")
    @JsonProperty("bookable_through")
    private LocalDate bookableThrough;

    @JsonProperty("picklist_id")
    private Long picklistId;
}