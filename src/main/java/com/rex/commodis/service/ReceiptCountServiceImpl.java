/** Service implementation for ReceiptCount business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.rex.commodis.dto.ReceiptStatusCountDto;
import com.rex.commodis.dto.ReceiptStatusCountProjection;
import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.repository.receipts.ReceiptRepository;

import java.util.EnumMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptCountServiceImpl implements ReceiptCountService{

    private final ReceiptRepository receiptRepository;

    public ReceiptStatusCountDto getSubmitterReceiptCounts(Long rexUserId){

        List<ReceiptStatusCountProjection> receiptStatusCountProjections = receiptRepository.getSubmitterStatusCounts(rexUserId);

        EnumMap<ReceiptStatusEnum, Long> receiptCounts = new EnumMap<>(ReceiptStatusEnum.class);

        for(ReceiptStatusEnum receiptStatus : ReceiptStatusEnum.values()){
            receiptCounts.put(receiptStatus, 0L);
        }

        for(ReceiptStatusCountProjection receiptStatusCountProjection : receiptStatusCountProjections){

            ReceiptStatusEnum receiptStatusValue = ReceiptStatusEnum.valueOf(receiptStatusCountProjection.getStatus());
            Long count = (receiptStatusCountProjection.getCount() != null) ? receiptStatusCountProjection.getCount() : 0L;

            receiptCounts.put(receiptStatusValue, count);
        }

        long total = receiptCounts.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();

        return ReceiptStatusCountDto.builder()
                .total(total)
                .byStatus(receiptCounts)
                .build();
    }

    public ReceiptStatusCountDto getApproverReceiptCounts(Long rexUserId){

        List<ReceiptStatusCountProjection> receiptStatusCountProjections = receiptRepository.getApproverStatusCounts(rexUserId);

        EnumMap<ReceiptStatusEnum, Long> receiptCounts = new EnumMap<>(ReceiptStatusEnum.class);

        for(ReceiptStatusEnum receiptStatus : ReceiptStatusEnum.values()){
            receiptCounts.put(receiptStatus, 0L);
        }

        for(ReceiptStatusCountProjection receiptStatusCountProjection : receiptStatusCountProjections){

            ReceiptStatusEnum receiptStatusValue = ReceiptStatusEnum.valueOf(receiptStatusCountProjection.getStatus());
            Long count = (receiptStatusCountProjection.getCount() != null) ? receiptStatusCountProjection.getCount() : 0L;

            receiptCounts.put(receiptStatusValue, count);
        }

        long total = receiptCounts.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();

        return ReceiptStatusCountDto.builder()
                .total(total)
                .byStatus(receiptCounts)
                .build();
    }
}
