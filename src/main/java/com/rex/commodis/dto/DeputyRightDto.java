/** Data transfer object carrying DeputyRight data between the API layer and the service/client. */
package com.rex.commodis.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeputyRightDto {
    private Long id;
    private String deputyRightDe;
    private String deputyRightEn;
}
