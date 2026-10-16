/** Service interface defining User operations; implemented by UserServiceImpl. */
package com.rex.commodis.security.service;

import com.rex.commodis.model.User;
import com.rex.commodis.security.dto.AuthenticatedUserDto;
import com.rex.commodis.security.dto.RegistrationRequest;
import com.rex.commodis.security.dto.RegistrationResponse;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
public interface UserService {

	User findByUsername(String username);

	RegistrationResponse registration(RegistrationRequest registrationRequest);

	AuthenticatedUserDto findAuthenticatedUserByUsername(String username);

}
