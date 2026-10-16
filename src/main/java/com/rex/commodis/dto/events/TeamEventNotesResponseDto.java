/** Response DTO returned by the TeamEventNotes API endpoint. */
package com.rex.commodis.dto.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamEventNotesResponseDto {

    private Long teamEventId;

    private String internalNote;
}