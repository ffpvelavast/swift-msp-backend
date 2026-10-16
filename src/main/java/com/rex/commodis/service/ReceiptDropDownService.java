/** Service interface defining ReceiptDropDown operations; implemented by ReceiptDropDownServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.ReceiptDropDownResponseDto;

public interface ReceiptDropDownService {

    ReceiptDropDownResponseDto getDropDowns(Long rexUserId, Long platformId);
}
