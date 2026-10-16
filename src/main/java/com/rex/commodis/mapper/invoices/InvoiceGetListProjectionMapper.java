/** Service interface defining InvoiceGetListProjectionMapper operations; implemented by InvoiceGetListProjectionMapperImpl. */
package com.rex.commodis.mapper.invoices;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.rex.commodis.dto.invoiceDtos.InvoiceGetListDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceGetListProjectionDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceGetListProjectionMapper {

    @Mapping(source = "vendor",               target = "vendorName")
    @Mapping(source = "documentName",         target = "invoiceTitle")
    @Mapping(source = "documentDate",           target = "invoiceDate")
    @Mapping(source = "vendorInvoiceNumber",  target = "invoiceNumber")
    @Mapping(source = "invoiceTotalGross",    target = "invoiceTotal")
    @Mapping(source = "status",               target = "status")
    InvoiceGetListDto toDto(InvoiceGetListProjectionDto projection);

    List<InvoiceGetListDto> toDtoList(List<InvoiceGetListProjectionDto> projections);
}