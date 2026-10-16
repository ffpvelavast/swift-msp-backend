/** Service interface defining InvoiceMapper operations; implemented by InvoiceMapperImpl. */
package com.rex.commodis.mapper.invoices;

import com.rex.commodis.dto.invoices.InvoiceDto;
import com.rex.commodis.model.invoices.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceMapper {
    Invoice toEntity(InvoiceDto invoiceDto);
    InvoiceDto toDto(Invoice invoice);
    List<InvoiceDto> toDtoList(List<Invoice> invoices);
}



