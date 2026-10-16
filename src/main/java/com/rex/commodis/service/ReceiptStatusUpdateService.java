/** Service interface defining ReceiptStatusUpdate operations; implemented by ReceiptStatusUpdateServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.ReceiptStatusUpdateRequestDto;
import com.rex.commodis.dto.receipts.ReceiptStatusUpdateResponseDto;

public interface ReceiptStatusUpdateService {

    /**
     * Updates the receipt status based on the approver's UI action.
     *
     * Business flow:
     *   1. Validate receipt exists
     *   2. Validate status string is an accepted value
     *   3. Resolve status string → receiptstatus_id via display_text JSONB lookup
     *   4. Deactivate the current active receipt_history row
     *   5. Insert a new receipt_history row with active = true
     *
     * @param receiptStatusUpdateRequestDto status update payload from the UI
     * @param rexUserId  path variable from URL (must match requestDto.receiptId)
     * @return response DTO reflecting the updated state
     */
    ReceiptStatusUpdateResponseDto updateReceiptStatus(
            ReceiptStatusUpdateRequestDto receiptStatusUpdateRequestDto,
            Long receiptId, Long rexUserId
    );
}