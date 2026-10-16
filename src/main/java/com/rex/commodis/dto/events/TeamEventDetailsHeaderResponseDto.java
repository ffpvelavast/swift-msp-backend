/** Response DTO returned by the TeamEventDetailsHeader API endpoint. */
package com.rex.commodis.dto.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamEventDetailsHeaderResponseDto {

    @JsonProperty("team_event_id")
    private Long teamEventId;

    @JsonProperty("projectNumber")
    private String projectNumber;

    @JsonProperty("event_title")
    private String eventTitle;

    @JsonProperty("event_date")
    private LocalDate eventDate;

    @JsonProperty("submitted_by")
    private String submittedBy;

    @JsonProperty("internal_note")
    private String internalNote;

    @JsonProperty("teameventtype_id")
    private Long teameventtypeId;

    @JsonProperty("bookable_through")
    private LocalDate bookableThrough;

    private String status;

    @JsonProperty("projectnumber_id")
    private Long projectNumberId;

    @JsonProperty("picklist_id")
    private Long picklistId;

    @JsonProperty("picklist_member_count")
    private Long picklistMemberCount;
}