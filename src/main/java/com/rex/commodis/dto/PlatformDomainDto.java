/** Data transfer object carrying PlatformDomain data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.*;

/**
 * Data Transfer Objects for REXBASE entities
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformDomainDto {
    private Long id;
    private Long platformId;
    private String domainName;
}

