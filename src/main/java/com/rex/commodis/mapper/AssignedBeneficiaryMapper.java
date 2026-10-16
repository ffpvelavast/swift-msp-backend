/** Service interface defining AssignedBeneficiaryMapper operations; implemented by AssignedBeneficiaryMapperImpl. */
package com.rex.commodis.mapper;

import org.mapstruct.*;

import com.rex.commodis.dto.AssignedBeneficiaryDto;
import com.rex.commodis.model.Beneficiary;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AssignedBeneficiaryMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "beneficiaryId", target = "beneficiaryId")
    @Mapping(source = "commodisId", target = "commodisId")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "academicTitle", target = "academicTitle")
    @Mapping(source = "listName", target = "listName")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "emailAddress", target = "emailAddress")
    @Mapping(source = "leavingDate", target = "leavingDate")
    @Mapping(source = "externalSystemId", target = "externalSystemId")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    AssignedBeneficiaryDto toDto(Beneficiary entity);

    List<AssignedBeneficiaryDto> toDtoList(List<Beneficiary> entities);
}