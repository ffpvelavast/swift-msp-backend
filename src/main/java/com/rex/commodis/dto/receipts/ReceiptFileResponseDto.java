/** Response DTO returned by the ReceiptFile API endpoint. */
package com.rex.commodis.dto.receipts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptFileResponseDto {

    @JsonProperty("sequence")
    private Integer sequence;

    @JsonProperty("file_url")
    private String fileUrl;

    @JsonProperty("media_type")
    private String mediaType;

}
