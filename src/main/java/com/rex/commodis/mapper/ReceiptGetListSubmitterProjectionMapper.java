/** Service interface defining ReceiptGetListSubmitterProjectionMapper operations; implemented by ReceiptGetListSubmitterProjectionMapperImpl. */
package com.rex.commodis.mapper;

import com.rex.commodis.dto.ReceiptGetListSubmitterDto;
import com.rex.commodis.dto.ReceiptGetListSubmitterProjectionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReceiptGetListSubmitterProjectionMapper{

    @Mapping(source = "id",            target = "receiptId")
    @Mapping(source = "receiptTitle",  target = "receiptTitle")
    @Mapping(source = "submittedBy",   target = "submittedBy")
    @Mapping(source = "receiptDate",   target = "receiptDate")
    @Mapping(source = "category",      target = "category")
    @Mapping(source = "receiptNumber", target = "receiptNumber")
    @Mapping(source = "receiptAmount", target = "receiptAmount")
    @Mapping(source = "currency",      target = "currency")
    @Mapping(source = "status",        target = "status")
    ReceiptGetListSubmitterDto toDto(ReceiptGetListSubmitterProjectionDto projection);

    List<ReceiptGetListSubmitterDto> toDtoList(List<ReceiptGetListSubmitterProjectionDto> projections);
}