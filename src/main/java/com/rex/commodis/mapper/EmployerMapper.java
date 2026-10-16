/** Service interface defining EmployerMapper operations; implemented by EmployerMapperImpl. */
package com.rex.commodis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.rex.commodis.dto.EmployerDto;
import com.rex.commodis.model.Employer;

import java.util.List;

/**
 * MapStruct mapper for Employer ↔ EmployerDto
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployerMapper {

    EmployerMapper INSTANCE = Mappers.getMapper(EmployerMapper.class);

    @Mapping(source = "employerUuid", target = "employerUuid")
    @Mapping(source = "platformId", target = "platformId")
    @Mapping(source = "displayName", target = "displayName")
    @Mapping(source = "activeFrom", target = "activeFrom")
    @Mapping(source = "activeThrough", target = "activeThrough")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    EmployerDto toDto(Employer entity);

    @Mapping(source = "employerUuid", target = "employerUuid")
    @Mapping(source = "platformId", target = "platformId")
    @Mapping(source = "displayName", target = "displayName")
    @Mapping(source = "activeFrom", target = "activeFrom")
    @Mapping(source = "activeThrough", target = "activeThrough")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    Employer toEntity(EmployerDto dto);

    List<EmployerDto> toDtoList(List<Employer> entities);

    List<Employer> toEntityList(List<EmployerDto> dtos);
}
