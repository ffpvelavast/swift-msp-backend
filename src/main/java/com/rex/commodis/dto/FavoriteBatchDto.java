/** Data transfer object carrying FavoriteBatch data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.Data;
import java.util.List;

@Data
public class FavoriteBatchDto {
    private List<Long> beneficiaryIds;
}
