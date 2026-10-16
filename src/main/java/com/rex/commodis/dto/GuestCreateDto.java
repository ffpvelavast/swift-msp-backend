/** Data transfer object carrying GuestCreate data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.Data;

@Data
public class GuestCreateDto {
    private String name;          // e.g. "Sophia Turner"
    private String organization;  // e.g. "Google"
    private String type;          // e.g. "Client"
    private String jobTitle;      // e.g. "Senior Consultant"
    private Boolean taxationAbroad; // true if taxed abroad, false otherwise
}