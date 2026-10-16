/** Service interface defining Picklist operations; implemented by PicklistServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.PicklistCreateDto;
import com.rex.commodis.dto.PicklistDto;
import com.rex.commodis.dto.PicklistMemberDto;
import com.rex.commodis.dto.PicklistCreateResponseDto;
import com.rex.commodis.dto.PicklistSyncMembersRequestDto;
import com.rex.commodis.dto.PicklistSyncMembersResponseDto;

import java.util.List;

/**
 * Service for Picklist operations
 */
public interface PicklistService {

    /**
     * Fetch members of a picklist using DB function
     */
    List<PicklistMemberDto> getPicklistMembers(Long picklistId, Long rexUserId);

    /**
     * List all picklists for a user
     */
    List<PicklistDto> getUserPicklists(Long rexUserId);

    /**
     * Create a new picklist with members.
     */
    PicklistCreateResponseDto createPicklist(PicklistCreateDto request, Long rexUserId);

    /**
     * Replace all favorites for the user in a single batch (ID 1).
     */
    void batchUpdateFavorites(List<Long> beneficiaryIds, Long rexUserId);

    /**
     * Wipe and reinsert all members for a picklist in one transaction.
     * Throws 403 if the picklist does not belong to rexUserId.
     */
    PicklistSyncMembersResponseDto syncMembers(Long picklistId, PicklistSyncMembersRequestDto request, Long rexUserId);

    /**
     * Delete a picklist by id after validation.
     * Throws 403 if user does not own it.
     * Throws 409 if picklist is in use.
     */
    void deletePicklistById(Long picklistId, Long rexUserId);
}