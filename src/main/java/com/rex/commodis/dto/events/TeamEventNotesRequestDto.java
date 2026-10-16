/** Request DTO for the TeamEventNotes operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto.events;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamEventNotesRequestDto {
    private String internalNote;
}