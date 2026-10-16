/** Service interface defining DeputyRightAssignmentMapper operations; implemented by DeputyRightAssignmentMapperImpl. */
//package com.rex.commodis.mapper;
//
//import com.rex.boilerplate.springboot.dto.DeputyRightAssignmentDto;
//import com.rex.boilerplate.springboot.model.DeputyRightAssignment;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.ReportingPolicy;
//import java.util.List;
//
//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
//public interface DeputyRightAssignmentMapper {
//
//    @Mapping(source = "deputyAssignmentId", target = "deputyAssignmentId")
//    @Mapping(source = "deputyRightId", target = "deputyRightId")
//    DeputyRightAssignmentDto toDto(DeputyRightAssignment entity);
//
//    @Mapping(source = "deputyAssignmentId", target = "deputyAssignmentId")
//    @Mapping(source = "deputyRightId", target = "deputyRightId")
//    DeputyRightAssignment toEntity(DeputyRightAssignmentDto dto);
//
//    List<DeputyRightAssignmentDto> toDtoList(List<DeputyRightAssignment> entities);
//
//    List<DeputyRightAssignment> toEntityList(List<DeputyRightAssignmentDto> dtos);
//}



