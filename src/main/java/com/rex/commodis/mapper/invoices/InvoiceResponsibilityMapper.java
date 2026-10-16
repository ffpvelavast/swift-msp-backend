/** Service interface defining InvoiceResponsibilityMapper operations; implemented by InvoiceResponsibilityMapperImpl. */
package com.rex.commodis.mapper.invoices;

import com.rex.commodis.dto.invoices.InvoiceResponsibilityDto;
import com.rex.commodis.model.invoices.InvoiceResponsibility;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceResponsibilityMapper {
    InvoiceResponsibility toEntity(InvoiceResponsibilityDto dto);
    InvoiceResponsibilityDto toDto(InvoiceResponsibility entity);
    List<InvoiceResponsibilityDto> toDtoList(List<InvoiceResponsibility> entities);
}



