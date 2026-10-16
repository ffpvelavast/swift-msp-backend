/** Data transfer object carrying GuestType data between the API layer and the service/client. */
package com.rex.commodis.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestTypeDto {
    private Long id;
    private String guestEnum;
    private String displayText;
    private Boolean businessGuest;
}
