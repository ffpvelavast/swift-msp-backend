/** Request DTO for the ReceiptEvent operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto.receipts;

import lombok.Data;

@Data
public class ReceiptEventRequestDto {

    private Long eventId;

    private String eventSource;
}
