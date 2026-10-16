/** Request DTO for the EmailCheck operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCheckRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(
            description = "Email address to check",
            example = "user@example.com"
    )
    private String email;
}