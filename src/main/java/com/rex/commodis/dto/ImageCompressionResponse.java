/** Response DTO returned by the ImageCompression API endpoint. */
package com.rex.commodis.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageCompressionResponse {
    private String originalFileName;
    private long originalSize;
    private String compressedFileName;
    private long compressedSize;
    private String url;
}
