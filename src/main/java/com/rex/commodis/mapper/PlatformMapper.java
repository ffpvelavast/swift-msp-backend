/** Service interface defining PlatformMapper operations; implemented by PlatformMapperImpl. */
package com.rex.commodis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.rex.commodis.dto.PlatformDto;
import com.rex.commodis.model.Platform;

import java.util.List;

/**
 * MapStruct mapper for Platform ↔ PlatformDto
 */
@Mapper(componentModel = "spring")
public interface PlatformMapper {

    PlatformMapper INSTANCE = Mappers.getMapper(PlatformMapper.class);

    PlatformDto toDto(Platform entity);

    @Mapping(target = "activePeriod", ignore = true)
    Platform toEntity(PlatformDto dto);

    List<PlatformDto> toDtoList(List<Platform> entities);

    List<Platform> toEntityList(List<PlatformDto> dtos);
}
