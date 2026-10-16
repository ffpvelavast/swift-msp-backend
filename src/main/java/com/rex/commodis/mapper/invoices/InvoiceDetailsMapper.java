/** Service interface defining InvoiceDetailsMapper operations; implemented by InvoiceDetailsMapperImpl. */
package com.rex.commodis.mapper.invoices;

import com.rex.commodis.dto.invoiceDtos.InvoicePositionResponseDto;
import com.rex.commodis.model.invoices.InvoicePosition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceDetailsMapper {

    InvoicePositionResponseDto toPositionResponseDto(InvoicePosition position);

    List<InvoicePositionResponseDto> toPositionResponseDtoList(List<InvoicePosition> positions);
}


