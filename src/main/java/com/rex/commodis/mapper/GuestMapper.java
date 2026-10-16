/** Service interface defining GuestMapper operations; implemented by GuestMapperImpl. */
package com.rex.commodis.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.rex.commodis.dto.GuestDto;
import com.rex.commodis.model.Guest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GuestMapper {

    // ENTITY -> DTO
    @Mapping(source = "defaultGuestType.id", target = "defaultGuesttypeId")
    @Mapping(source = "creatorRexUserId", target = "creatorRexUserId")
    GuestDto toDto(Guest entity);

    List<GuestDto> toDto(List<Guest> entities);

    // DTO -> ENTITY (for CREATE). DB manages id, listname, created/updated
    // timestamps.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "listname", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "platformId", ignore = true) // Set in service or via other means
    @Mapping(source = "defaultGuesttypeId", target = "defaultGuestType.id")
    @Mapping(source = "creatorRexUserId", target = "creatorRexUserId")
    Guest toEntity(GuestDto dto);

    // Partial UPDATE (PATCH semantics) — ignore nulls so we don't overwrite
    // existing values.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "listname", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true) // DB-managed
    @Mapping(target = "platformId", ignore = true)
    @Mapping(source = "defaultGuesttypeId", target = "defaultGuestType.id")
    @Mapping(source = "creatorRexUserId", target = "creatorRexUserId")
    void updateFromDto(GuestDto dto, @MappingTarget Guest entity);
}
