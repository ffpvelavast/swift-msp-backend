package com.rex.commodis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PicklistSyncMembersRequestDto {
    private String name;
    private List<Long> beneficiaryIds;
    private List<Long> guestIds;
}
