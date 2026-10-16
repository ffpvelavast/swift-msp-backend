/** Service interface defining UserMapper operations; implemented by UserMapperImpl. */
package com.rex.commodis.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.rex.commodis.model.User;
import com.rex.commodis.security.dto.AuthenticatedUserDto;
import com.rex.commodis.security.dto.RegistrationRequest;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	User convertToUser(RegistrationRequest registrationRequest);

	AuthenticatedUserDto convertToAuthenticatedUserDto(User user);

	User convertToUser(AuthenticatedUserDto authenticatedUserDto);

}
