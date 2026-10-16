/** Data transfer object carrying AuthenticatedUser data between the API layer and the service/client. */
package com.rex.commodis.security.dto;

import com.rex.commodis.model.SecurityRole;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedUserDto {

	private String name;

	private String username;

	private String password;

	private SecurityRole userRole;

}
