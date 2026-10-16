/** Service implementation for BeneficiaryOrgData business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.dto.BeneficiaryOrgDataDto;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.mapper.BeneficiaryOrgDataMapper;
import com.rex.commodis.model.BeneficiaryOrgData;
import com.rex.commodis.repository.BeneficiaryOrgDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeneficiaryOrgDataServiceImpl implements BeneficiaryOrgDataService {

    private final BeneficiaryOrgDataRepository orgDataRepository;
    private final BeneficiaryOrgDataMapper orgDataMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BeneficiaryOrgDataDto> fetchByBeneficiaryId(Long beneficiaryId) {
        List<BeneficiaryOrgData> list = orgDataRepository.findAll().stream()
                .filter(o -> o.getBeneficiaryId().equals(beneficiaryId))
                .collect(Collectors.toList());
        return list.stream()
                .map(orgDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BeneficiaryOrgDataDto create(BeneficiaryOrgDataDto dto) {
        BeneficiaryOrgData entity = orgDataMapper.toEntity(dto);
        entity.setId(null);
        BeneficiaryOrgData saved = orgDataRepository.save(entity);
        return orgDataMapper.toDto(saved);
    }

    @Override
    @Transactional
    public BeneficiaryOrgDataDto update(Long id, BeneficiaryOrgDataDto dto) {
        BeneficiaryOrgData existing = orgDataRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BeneficiaryOrgData not found with id: " + id));
        // apply updates
        existing.setValidFrom(dto.getValidFrom());
        existing.setValidThrough(dto.getValidThrough());
        existing.setPersonnelNumberSystem(dto.getPersonnelNumberSystem());
        existing.setPersonnelNumberPayroll(dto.getPersonnelNumberPayroll());
        existing.setIdentityTag(dto.getIdentityTag());
        existing.setCompanySite(dto.getCompanySite());
        existing.setOrgUnit(dto.getOrgUnit());
        existing.setCostCenter(dto.getCostCenter());
        // updatedAt is handled by @UpdateTimestamp
        BeneficiaryOrgData updated = orgDataRepository.save(existing);
        return orgDataMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!orgDataRepository.existsById(id)) {
            throw new NotFoundException("BeneficiaryOrgData not found with id: " + id);
        }
        orgDataRepository.deleteById(id);
    }
}
