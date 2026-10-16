/** Service interface defining PlatformDomainMapper operations; implemented by PlatformDomainMapperImpl. */
package com.rex.commodis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.rex.commodis.dto.PlatformDomainDto;
import com.rex.commodis.model.PlatformDomain;

import java.util.List;

/**
 * MapStruct mapper for PlatformDomain ↔ PlatformDomainDto
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PlatformDomainMapper {

    PlatformDomainMapper INSTANCE = Mappers.getMapper(PlatformDomainMapper.class);

    @Mapping(source = "platformId", target = "platformId")
    @Mapping(source = "domainName", target = "domainName")
    PlatformDomainDto toDto(PlatformDomain entity);

    @Mapping(source = "platformId", target = "platformId")
    @Mapping(source = "domainName", target = "domainName")
    PlatformDomain toEntity(PlatformDomainDto dto);

    List<PlatformDomainDto> toDtoList(List<PlatformDomain> entities);
    List<PlatformDomain> toEntityList(List<PlatformDomainDto> dtos);
}
