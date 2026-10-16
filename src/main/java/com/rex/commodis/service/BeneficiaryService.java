/** Service interface defining Beneficiary operations; implemented by BeneficiaryServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.BeneficiaryDto;
import com.rex.commodis.dto.BeneficiaryFilterRequest;
import com.rex.commodis.dto.BeneficiaryPageResponse;

public interface BeneficiaryService {
    BeneficiaryPageResponse fetchBeneficiaries(BeneficiaryFilterRequest filterRequest);

    BeneficiaryDto getBeneficiaryById(java.util.UUID id);

    java.util.List<BeneficiaryDto> searchBeneficiariesByPlatform(Long platformId);
}
