/** Service interface defining DeputyAssignmentMapper operations; implemented by DeputyAssignmentMapperImpl. */
package com.rex.commodis.mapper;

import com.rex.commodis.dto.DeputyAssignmentDto;
import com.rex.commodis.model.DeputyAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * MapStruct mapper for DeputyAssignment ↔ DeputyAssignmentDto
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DeputyAssignmentMapper {

    DeputyAssignmentMapper INSTANCE = Mappers.getMapper(DeputyAssignmentMapper.class);

    @Mapping(source = "activeFrom",             target = "activeFrom")
    @Mapping(source = "activeThrough",          target = "activeThrough")
    @Mapping(source = "rexuserId",              target = "rexUserId")
    @Mapping(source = "deputyId",              target = "deputyId")
    @Mapping(source = "createdAt",             target = "createdAt")
    @Mapping(source = "updatedAt",             target = "updatedAt")
    DeputyAssignmentDto toDto(DeputyAssignment entity);

    @Mapping(source = "activeFrom",             target = "activeFrom")
    @Mapping(source = "activeThrough",          target = "activeThrough")
    @Mapping(source = "rexUserId",             target = "rexuserId")
    @Mapping(source = "deputyId",              target = "deputyId")
    @Mapping(source = "createdAt",             target = "createdAt")
    @Mapping(source = "updatedAt",             target = "updatedAt")
    DeputyAssignment toEntity(DeputyAssignmentDto dto);

    List<DeputyAssignmentDto> toDtoList(List<DeputyAssignment> entities);
    List<DeputyAssignment> toEntityList(List<DeputyAssignmentDto> dtos);
}
