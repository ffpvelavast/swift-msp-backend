/** Request DTO for the ReceiptUpdateNotes operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptUpdateNotesRequestDto {

    @JsonProperty("externalNote")
    private String externalNote;

    @JsonProperty("internalNote")
    private String internalNote;
}
