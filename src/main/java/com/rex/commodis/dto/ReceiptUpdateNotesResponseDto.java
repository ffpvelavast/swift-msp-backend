/** Response DTO returned by the ReceiptUpdateNotes API endpoint. */
package com.rex.commodis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptUpdateNotesResponseDto {

    @JsonProperty("externalNote")
    private String externalNote;

    @JsonProperty("internalNote")
    private String internalNote;
}
