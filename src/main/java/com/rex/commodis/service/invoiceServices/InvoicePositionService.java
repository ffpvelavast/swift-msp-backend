package com.rex.commodis.service.invoiceServices;

import com.rex.commodis.dto.invoiceDtos.SavePositionsRequestDto;
import com.rex.commodis.dto.invoiceDtos.SavePositionsResponseDto;

import java.util.Set;

public interface InvoicePositionService {

    Set<String> findDistinctProjectNumbers(Long invoiceId);

    boolean existsByProjectNumberId(Long projectNumberId);

    SavePositionsResponseDto savePositions(Long invoiceId, Long rexUserId, SavePositionsRequestDto requestDto);
}
