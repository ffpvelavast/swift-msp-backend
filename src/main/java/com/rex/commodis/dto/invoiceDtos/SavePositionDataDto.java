package com.rex.commodis.dto.invoiceDtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SavePositionDataDto {

    private Long invoicePositionId;
    private Long taxReasonId;
    private List<Long> beneficiaryIds;
    private List<Long> guestIds;
}
