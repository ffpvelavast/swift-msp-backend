/** Service interface defining PicklistToBeneficiaryMapper operations; implemented by PicklistToBeneficiaryMapperImpl. */
package com.rex.commodis.mapper;

import org.mapstruct.Mapper;

import com.rex.commodis.dto.PicklistToBeneficiaryDto;
import com.rex.commodis.model.PicklistToBeneficiary;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PicklistToBeneficiaryMapper {

    PicklistToBeneficiaryDto toDto(PicklistToBeneficiary entity);

    @org.mapstruct.Mapping(target = "beneficiary", ignore = true)
    @org.mapstruct.Mapping(target = "picklist", ignore = true)
    PicklistToBeneficiary toEntity(PicklistToBeneficiaryDto dto);

    List<PicklistToBeneficiaryDto> toDtoList(List<PicklistToBeneficiary> entities);

    List<PicklistToBeneficiary> toEntityList(List<PicklistToBeneficiaryDto> dtos);
}
