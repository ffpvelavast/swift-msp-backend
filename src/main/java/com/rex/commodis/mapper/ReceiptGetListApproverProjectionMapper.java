/** Service interface defining ReceiptGetListApproverProjectionMapper operations; implemented by ReceiptGetListApproverProjectionMapperImpl. */
package com.rex.commodis.mapper;

import com.rex.commodis.dto.ReceiptGetListApproverDto;
import com.rex.commodis.dto.ReceiptGetListApproverProjectionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReceiptGetListApproverProjectionMapper {

    @Mapping(source = "id",            target = "receiptId")
    @Mapping(source = "receiptTitle",  target = "receiptTitle")
    @Mapping(source = "submittedFor",   target = "submittedFor")
    @Mapping(source = "receiptDate",   target = "receiptDate")
    @Mapping(source = "category",      target = "category")
    @Mapping(source = "receiptNumber", target = "receiptNumber")
    @Mapping(source = "receiptAmount", target = "receiptAmount")
    @Mapping(source = "currency",      target = "currency")
    @Mapping(source = "status",        target = "status")
    ReceiptGetListApproverDto toDto(ReceiptGetListApproverProjectionDto projection);

    List<ReceiptGetListApproverDto> toDtoList(List<ReceiptGetListApproverProjectionDto> projections);
}