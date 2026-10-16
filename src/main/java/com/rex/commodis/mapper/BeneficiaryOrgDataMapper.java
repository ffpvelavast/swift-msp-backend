/** Service interface defining BeneficiaryOrgDataMapper operations; implemented by BeneficiaryOrgDataMapperImpl. */
package com.rex.commodis.mapper;

import com.rex.commodis.dto.BeneficiaryOrgDataDto;
import com.rex.commodis.model.BeneficiaryOrgData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * MapStruct mapper for BeneficiaryOrgData ↔ BeneficiaryOrgDataDto
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BeneficiaryOrgDataMapper {

    BeneficiaryOrgDataMapper INSTANCE = Mappers.getMapper(BeneficiaryOrgDataMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "beneficiaryId", target = "beneficiaryId")
    @Mapping(source = "employerId", target = "employerId")
    @Mapping(source = "validFrom", target = "validFrom")
    @Mapping(source = "validThrough", target = "validThrough")
    @Mapping(source = "personnelNumberSystem", target = "personnelNumberSystem")
    @Mapping(source = "personnelNumberPayroll", target = "personnelNumberPayroll")
    @Mapping(source = "identityTag", target = "identityTag")
    @Mapping(source = "companySite", target = "companySite")
    @Mapping(source = "orgUnit", target = "orgUnit")
    @Mapping(source = "costCenter", target = "costCenter")
    @Mapping(source = "createdAt", target = "createdAt")
    BeneficiaryOrgDataDto toDto(BeneficiaryOrgData entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "beneficiaryId", target = "beneficiaryId")
    @Mapping(source = "employerId", target = "employerId")
    @Mapping(source = "validFrom", target = "validFrom")
    @Mapping(source = "validThrough", target = "validThrough")
    @Mapping(source = "personnelNumberSystem", target = "personnelNumberSystem")
    @Mapping(source = "personnelNumberPayroll", target = "personnelNumberPayroll")
    @Mapping(source = "identityTag", target = "identityTag")
    @Mapping(source = "companySite", target = "companySite")
    @Mapping(source = "orgUnit", target = "orgUnit")
    @Mapping(source = "costCenter", target = "costCenter")
    @Mapping(source = "createdAt", target = "createdAt")
    BeneficiaryOrgData toEntity(BeneficiaryOrgDataDto dto);

    List<BeneficiaryOrgDataDto> toDtoList(List<BeneficiaryOrgData> entities);

    List<BeneficiaryOrgData> toEntityList(List<BeneficiaryOrgDataDto> dtos);
}
