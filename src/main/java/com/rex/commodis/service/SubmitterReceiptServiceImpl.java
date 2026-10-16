package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.SubmitterReceiptDto;
import com.rex.commodis.repository.receipts.SubmitterReceiptProjection;
import com.rex.commodis.repository.receipts.SubmitterReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmitterReceiptServiceImpl implements SubmitterReceiptService {

    private final SubmitterReceiptRepository submitterReceiptRepository;
    private final RexUserService rexUserService;

    @Override
    public List<SubmitterReceiptDto> getSubmitterReceipts(Long rexUserId) {

        String locale = rexUserService.getLocale(rexUserId);

        List<SubmitterReceiptProjection> receipts =
                submitterReceiptRepository.fetchSubmitterReceipts(rexUserId, locale);

        return receipts.stream()
                .map(receipt -> {

                    SubmitterReceiptDto dto = new SubmitterReceiptDto();
                    dto.setReceiptId(receipt.getReceiptId());
                    dto.setReceiptTitle(receipt.getReceiptTitle());
                    dto.setCategory(receipt.getCategory());
                    dto.setAmount(receipt.getAmount());
                    dto.setCurrency(receipt.getCurrency());
                    dto.setStatus(receipt.getStatus());

                    return dto;
                })
                .toList();
    }
}