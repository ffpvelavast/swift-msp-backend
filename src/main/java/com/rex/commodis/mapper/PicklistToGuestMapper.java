/** Service interface defining PicklistToGuestMapper operations; implemented by PicklistToGuestMapperImpl. */
package com.rex.commodis.mapper;

import org.mapstruct.Mapper;

import java.util.List;

import org.mapstruct.ReportingPolicy;

import com.rex.commodis.dto.PicklistToGuestDto;
import com.rex.commodis.model.PicklistToGuest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PicklistToGuestMapper {

    PicklistToGuestDto toDto(PicklistToGuest entity);

    PicklistToGuest toEntity(PicklistToGuestDto dto);

    List<PicklistToGuestDto> toDtoList(List<PicklistToGuest> entities);

    List<PicklistToGuest> toEntityList(List<PicklistToGuestDto> dtos);
}
