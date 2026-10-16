/** Service interface defining BeneficiaryOrgData operations; implemented by BeneficiaryOrgDataServiceImpl. */
package com.rex.commodis.service;

import java.util.List;

import com.rex.commodis.dto.BeneficiaryOrgDataDto;

/**
 * Service interface for managing BeneficiaryOrgData entities.
 */
public interface BeneficiaryOrgDataService {

    /**
     * Fetch all organizational data entries for a given beneficiary account.
     * 
     * @param beneficiaryAccountId the ID of the beneficiary account
     * @return list of BeneficiaryOrgDataDto
     */
    List<BeneficiaryOrgDataDto> fetchByBeneficiaryId(Long beneficiaryId);

    /**
     * Create a new organizational data record for a beneficiary.
     * 
     * @param dto the data to create
     * @return the created BeneficiaryOrgDataDto
     */
    BeneficiaryOrgDataDto create(BeneficiaryOrgDataDto dto);

    /**
     * Update an existing organizational data record.
     * 
     * @param id  the ID of the record to update
     * @param dto updated data
     * @return the updated BeneficiaryOrgDataDto
     */
    BeneficiaryOrgDataDto update(Long id, BeneficiaryOrgDataDto dto);

    /**
     * Delete an organizational data record by its ID.
     * 
     * @param id the ID of the record to delete
     */
    void delete(Long id);
}
