/** Service interface defining BeneficiaryMapper operations; implemented by BeneficiaryMapperImpl. */


// BeneficiaryMapper.java
package com.rex.commodis.mapper;

import com.rex.commodis.dto.AssignedBeneficiaryDto;
import com.rex.commodis.dto.BeneficiaryDto;
import com.rex.commodis.model.Beneficiary;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BeneficiaryMapper {

    BeneficiaryMapper INSTANCE = Mappers.getMapper(BeneficiaryMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "beneficiaryId", target = "beneficiaryId")
    @Mapping(source = "commodisId", target = "commodisId")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "academicTitle", target = "academicTitle")
    @Mapping(source = "listName", target = "listName")
    @Mapping(source = "leavingDate", target = "leavingDate")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "externalSystemId", target = "externalSystemId")
    @Mapping(target = "identityTag", ignore = true)
    @Mapping(target = "orgUnit", ignore = true)
    @Mapping(target = "costCenter", ignore = true)
    @Mapping(target = "employerName", ignore = true)
    BeneficiaryDto toDto(Beneficiary entity);

    @InheritInverseConfiguration
    @Mapping(target = "platform", ignore = true)
    @Mapping(target = "rexUser", ignore = true)
    Beneficiary toEntity(BeneficiaryDto dto);

    List<BeneficiaryDto> toDtoList(List<Beneficiary> entities);

    List<Beneficiary> toEntityList(List<BeneficiaryDto> dtos);
}

