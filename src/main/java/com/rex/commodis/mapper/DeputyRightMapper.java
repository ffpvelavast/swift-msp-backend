/** Service interface defining DeputyRightMapper operations; implemented by DeputyRightMapperImpl. */
//package com.rex.commodis.mapper;
//
//import com.rex.boilerplate.springboot.dto.DeputyRightDto;
//import com.rex.boilerplate.springboot.model.DeputyRight;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//import java.util.List;
//
///**
// * MapStruct mapper for DeputyRight ↔ DeputyRightDto
// */
//@Mapper(componentModel = "spring")
//public interface DeputyRightMapper {
//
//    DeputyRightMapper INSTANCE = Mappers.getMapper(DeputyRightMapper.class);
//
//    @Mapping(source = "deputyRightDe", target = "deputyRightDe")
//    @Mapping(source = "deputyRightEn", target = "deputyRightEn")
//    DeputyRightDto toDto(DeputyRight entity);
//
//    @Mapping(source = "deputyRightDe", target = "deputyRightDe")
//    @Mapping(source = "deputyRightEn", target = "deputyRightEn")
//    DeputyRight toEntity(DeputyRightDto dto);
//
//    List<DeputyRightDto> toDtoList(List<DeputyRight> entities);
//    List<DeputyRight> toEntityList(List<DeputyRightDto> dtos);
//}



