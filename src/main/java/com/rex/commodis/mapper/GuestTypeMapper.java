/** Service interface defining GuestTypeMapper operations; implemented by GuestTypeMapperImpl. */
package com.rex.commodis.mapper;

import com.rex.commodis.dto.GuestTypeDto;
import com.rex.commodis.model.GuestType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * MapStruct mapper for GuestType ↔ GuestTypeDto
 */
@Mapper(componentModel = "spring")
public interface GuestTypeMapper {

    GuestTypeMapper INSTANCE = Mappers.getMapper(GuestTypeMapper.class);

    @Mapping(source = "guestEnum",    target = "guestEnum")
    @Mapping(source = "displayText", target = "displayText")
    @Mapping(source = "businessGuest", target = "businessGuest")
    GuestTypeDto toDto(GuestType entity);

    @Mapping(source = "guestEnum",    target = "guestEnum")
    @Mapping(source = "displayText", target = "displayText")
    @Mapping(source = "businessGuest", target = "businessGuest")
    GuestType toEntity(GuestTypeDto dto);

    List<GuestTypeDto> toDtoList(List<GuestType> entities);
    List<GuestType> toEntityList(List<GuestTypeDto> dtos);
}
