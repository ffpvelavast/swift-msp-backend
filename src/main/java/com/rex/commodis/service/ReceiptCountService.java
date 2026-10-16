/** Service interface defining ReceiptCount operations; implemented by ReceiptCountServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.ReceiptStatusCountDto;

public interface ReceiptCountService {

    ReceiptStatusCountDto getSubmitterReceiptCounts(Long rexUserId);

    ReceiptStatusCountDto getApproverReceiptCounts(Long rexUserId);
}
