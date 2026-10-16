/** Service implementation for ReceiptToBeneficiary business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.model.receipts.receiptToBeneficiary.ReceiptToBeneficiary;
import com.rex.commodis.repository.receipts.ReceiptToBeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptToBeneficiaryServiceImpl implements ReceiptToBeneficiaryService{

    private final ReceiptToBeneficiaryRepository receiptToBeneficiaryRepository;

    @Override
    @Transactional
    public void saveBeneficiaries(Long receiptId, List<Long> beneficiaryIds){

        if(beneficiaryIds == null || beneficiaryIds.isEmpty())
            return;

        List<ReceiptToBeneficiary> receiptToBeneficiaries = beneficiaryIds.stream()
                .map(beneficiaryId -> ReceiptToBeneficiary.builder()
                        .receiptId(receiptId)
                        .beneficiaryId(beneficiaryId)
                        .build())
                .toList();

        receiptToBeneficiaryRepository.saveAll(receiptToBeneficiaries);
    }
}
