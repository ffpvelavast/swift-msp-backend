/** Service interface defining ReceiptToBeneficiary operations; implemented by ReceiptToBeneficiaryServiceImpl. */
package com.rex.commodis.service;

import java.util.List;

public interface ReceiptToBeneficiaryService {

    void saveBeneficiaries(Long receiptId, List<Long> beneficiaryIds);
}
