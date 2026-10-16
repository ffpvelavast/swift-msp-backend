/** Data transfer object carrying PicklistCreate data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating a new picklist with its members.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PicklistCreateDto {

    private String name;
    
    private List<Long> beneficiaryIds;
    
    private List<Long> guestIds;
}
