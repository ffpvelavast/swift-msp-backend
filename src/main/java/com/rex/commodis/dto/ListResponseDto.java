/** Response DTO returned by the List API endpoint. */
package com.rex.commodis.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListResponseDto<T, M> {
    private M meta;
    private List<T> data;
}
