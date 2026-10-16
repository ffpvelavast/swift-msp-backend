/** Data transfer object carrying BookableEvent data between the API layer and the service/client. */
package com.rex.commodis.dto.receipts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookableEventDto {

    private String eventSource;
    private Long eventId;
    private String eventTitle;
    private Boolean inheritParticipants;
}
