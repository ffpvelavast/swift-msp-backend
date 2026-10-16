/** Service interface defining PicklistToBeneficiary operations; implemented by PicklistToBeneficiaryServiceImpl. */
package com.rex.commodis.service;

import java.util.List;

import com.rex.commodis.dto.PicklistToBeneficiaryDto;

/**
 * Service interface for managing PicklistToBeneficiary entities.
 */
public interface PicklistToBeneficiaryService {

    /**
     * Fetch all PicklistToBeneficiary links for a specific picklist.
     * @param picklistId the picklist ID
     * @return list of PicklistToBeneficiaryDto
     */
    List<PicklistToBeneficiaryDto> fetchByPicklistId(Long picklistId);

    /**
     * Create a new PicklistToBeneficiary link.
     * @param dto data for the new link
     * @return the created PicklistToBeneficiaryDto
     */
    PicklistToBeneficiaryDto create(PicklistToBeneficiaryDto dto);

    /**
     * Delete a PicklistToBeneficiary link by its ID.
     * @param id the link ID
     */
    void delete(Long id);
}
