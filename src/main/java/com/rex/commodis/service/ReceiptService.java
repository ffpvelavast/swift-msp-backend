/** Service interface defining Receipt operations; implemented by ReceiptServiceImpl. */
package com.rex.commodis.service;

import java.util.List;

import com.rex.commodis.dto.receipts.CreateReceiptResponseDto;
import org.springframework.web.multipart.MultipartFile;

import com.rex.commodis.dto.receipts.CreateReceiptRequestDto;

public interface ReceiptService {

    CreateReceiptResponseDto createReceipt(CreateReceiptRequestDto request, Long platformId,
                                           Long receiptCreatorId, List<MultipartFile> files);

    public boolean existisByProjectNumber(Long projectNumberId);

}
