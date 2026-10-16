/** Service interface defining ReceiptSidePanel operations; implemented by ReceiptSidePanelServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.ReceiptSidePanelResponseDto;

public interface ReceiptSidePanelService {
    ReceiptSidePanelResponseDto getReceiptSidePanel(Long receiptId, Long rexUserId);
}
