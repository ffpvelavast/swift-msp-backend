/** Service interface defining BeneficiaryToEmployer operations; implemented by BeneficiaryToEmployerServiceImpl. */
package com.rex.commodis.service;

import java.util.List;

import com.rex.commodis.dto.BeneficiaryToEmployerDto;

/**
 * Service interface for managing Beneficiary to Employer relationships.
 */
public interface BeneficiaryToEmployerService {

    /**
     * Fetch all link records for a given beneficiary ID.
     *
     * @param beneficiaryId the ID of the beneficiary
     * @return list of BeneficiaryToEmployerDto
     */
    List<BeneficiaryToEmployerDto> fetchByBeneficiaryId(Long beneficiaryId);

    /**
     * Fetch all link records for a given employer ID.
     *
     * @param employerId the ID of the employer
     * @return list of BeneficiaryToEmployerDto
     */
    List<BeneficiaryToEmployerDto> fetchByEmployerId(Long employerId);
}
