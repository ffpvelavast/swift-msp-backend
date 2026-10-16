/** REST controller exposing Registration API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rex.commodis.security.dto.RegistrationRequest;
import com.rex.commodis.security.dto.RegistrationResponse;
import com.rex.commodis.security.service.UserService;


/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegistrationController {

	private final UserService userService;

	@PostMapping
	@Operation(tags = "Register Service", description = "You can register to the system by sending information in the appropriate format.")
	public ResponseEntity<RegistrationResponse> registrationRequest(@Valid @RequestBody RegistrationRequest registrationRequest) {

		System.out.println("In Registration Request : "+registrationRequest.getEmail());

		final RegistrationResponse registrationResponse = userService.registration(registrationRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
	}

}
