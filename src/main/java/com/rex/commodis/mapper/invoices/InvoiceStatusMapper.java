/** Service interface defining InvoiceStatusMapper operations; implemented by InvoiceStatusMapperImpl. */
package com.rex.commodis.mapper.invoices;

import com.rex.commodis.dto.invoices.InvoiceStatusDto;
import com.rex.commodis.model.invoices.InvoiceStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceStatusMapper {
    InvoiceStatus toEntity(InvoiceStatusDto dto);
    InvoiceStatusDto toDto(InvoiceStatus entity);
    List<InvoiceStatusDto> toDtoList(List<InvoiceStatus> entities);
}



