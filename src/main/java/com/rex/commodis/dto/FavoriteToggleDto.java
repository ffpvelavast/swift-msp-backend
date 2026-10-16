/** Data transfer object carrying FavoriteToggle data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.Data;

@Data
public class FavoriteToggleDto {
    private Long beneficiaryId;
}
