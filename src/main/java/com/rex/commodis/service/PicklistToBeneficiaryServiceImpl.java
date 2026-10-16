/** Service implementation for PicklistToBeneficiary business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.dto.PicklistToBeneficiaryDto;
import com.rex.commodis.mapper.PicklistToBeneficiaryMapper;
import com.rex.commodis.model.PicklistToBeneficiary;
import com.rex.commodis.model.PicklistToBeneficiaryId;
import com.rex.commodis.repository.PicklistToBeneficiaryRepository;

@Service
public class PicklistToBeneficiaryServiceImpl {

    private final PicklistToBeneficiaryRepository repository;
    private final PicklistToBeneficiaryMapper mapper;

    public PicklistToBeneficiaryServiceImpl(
            PicklistToBeneficiaryRepository repository,
            PicklistToBeneficiaryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Create or upsert a link between a picklist and a beneficiary.
     * DB sets created_at (DEFAULT LOCALTIMESTAMP); we don't touch it.
     */
    @Transactional
    public PicklistToBeneficiaryDto save(PicklistToBeneficiaryDto dto) {
        PicklistToBeneficiary entity = mapper.toEntity(dto);
        // Ensure we never try to set createdAt from client; let DB default apply
        entity.setCreatedAt(null);

        PicklistToBeneficiary saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public PicklistToBeneficiaryDto get(Long picklistId, Long beneficiaryId) {
        PicklistToBeneficiaryId id = new PicklistToBeneficiaryId(picklistId, beneficiaryId);
        PicklistToBeneficiary entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Link not found for picklistId=" + picklistId + ", beneficiaryId=" + beneficiaryId));
        return mapper.toDto(entity);
    }

    @Transactional
    public void delete(Long picklistId, Long beneficiaryId) {
        PicklistToBeneficiaryId id = new PicklistToBeneficiaryId(picklistId, beneficiaryId);
        repository.deleteById(id);
    }
}
