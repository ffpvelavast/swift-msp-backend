/** Service interface defining InvoicePositionMapper operations; implemented by InvoicePositionMapperImpl. */
package com.rex.commodis.mapper.invoices;

import com.rex.commodis.dto.invoices.InvoicePositionDto;
import com.rex.commodis.model.invoices.InvoicePosition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoicePositionMapper {
    InvoicePosition toEntity(InvoicePositionDto invoicePositionDto);
    InvoicePositionDto toDto(InvoicePosition invoicePosition);
    List<InvoicePositionDto> toDtoList(List<InvoicePosition> invoicePositions);
}



