/** Service interface defining ReceiptNotes operations; implemented by ReceiptNotesServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.ReceiptUpdateNotesRequestDto;
import com.rex.commodis.dto.ReceiptUpdateNotesResponseDto;

public interface ReceiptNotesService {

    ReceiptUpdateNotesResponseDto updateReceiptNotes(Long receiptId, Long rexUserId, ReceiptUpdateNotesRequestDto request);

}
