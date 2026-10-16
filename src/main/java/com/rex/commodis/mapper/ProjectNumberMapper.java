/** Service interface defining ProjectNumberMapper operations; implemented by ProjectNumberMapperImpl. */
package com.rex.commodis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.rex.commodis.dto.ProjectNumberDto;
import com.rex.commodis.model.ProjectNumber;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectNumberMapper {
    ProjectNumberDto toDto(ProjectNumber entity);

    ProjectNumber toEntity(ProjectNumberDto dto);
}
