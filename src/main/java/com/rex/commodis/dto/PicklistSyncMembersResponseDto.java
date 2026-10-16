package com.rex.commodis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PicklistSyncMembersResponseDto {
    private Long picklistId;
    private String picklistName;
    private List<Long> beneficiaryIds;
    private List<Long> guestIds;
}
