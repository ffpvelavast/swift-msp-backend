/** Service interface defining ProjectLeaderMapper operations; implemented by ProjectLeaderMapperImpl. */
package com.rex.commodis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.rex.commodis.dto.ProjectLeaderDto;
import com.rex.commodis.model.ProjectLeader;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectLeaderMapper {
    ProjectLeaderDto toDto(ProjectLeader entity);

    ProjectLeader toEntity(ProjectLeaderDto dto);
}
