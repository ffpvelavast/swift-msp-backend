/** DTO carrying a locale update request; maps to the PATCH /api/identity/locale request body. */
package com.rex.commodis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocaleRequestDto {

    @NotBlank(message = "Locale must not be blank")
    @Size(min = 2, max = 2, message = "Locale must be a 2-character language code")
    @Pattern(regexp = "[a-zA-Z]{2}", message = "Locale must be a valid 2-letter language code (e.g. 'en', 'de')")
    private String locale;
}
