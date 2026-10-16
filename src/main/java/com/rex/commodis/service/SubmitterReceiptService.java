package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.SubmitterReceiptDto;

import java.util.List;

public interface SubmitterReceiptService {

    List<SubmitterReceiptDto> getSubmitterReceipts(Long rexUserId);
}