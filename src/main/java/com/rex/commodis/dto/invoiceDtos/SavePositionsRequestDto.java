package com.rex.commodis.dto.invoiceDtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SavePositionsRequestDto {

    private String note;
    private List<SavePositionDataDto> invoicePositionData;
}
