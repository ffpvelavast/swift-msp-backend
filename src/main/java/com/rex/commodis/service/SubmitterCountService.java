package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.SubmitterCountDto;

public interface SubmitterCountService {

    SubmitterCountDto getSubmitterReceiptCount(Long rexUserId);
}