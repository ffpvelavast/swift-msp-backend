/** Service interface defining GetDelegatedInvoiceMapper operations; implemented by GetDelegatedInvoiceMapperImpl. */
package com.rex.commodis.mapper.invoices;

import com.rex.commodis.dto.invoiceDtos.InvoiceDelegateDetailsDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceDelegateDetailsProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GetDelegatedInvoiceMapper {

    @Mapping(source = "vendor",              target = "vendorName")
    @Mapping(source = "documentName",        target = "invoiceTitle")
    @Mapping(source = "documentDate",        target = "invoiceDate")
    @Mapping(source = "documentNumber",      target = "invoiceNumber")
    @Mapping(source = "invoiceTotalGross",   target = "invoiceTotal")
    @Mapping(source = "delegateId",          target = "delegatedUserId")
    @Mapping(source = "status", target = "status")
    InvoiceDelegateDetailsDto toDto(InvoiceDelegateDetailsProjection projection);

    List<InvoiceDelegateDetailsDto> toDtoList(List<InvoiceDelegateDetailsProjection> projections);
}