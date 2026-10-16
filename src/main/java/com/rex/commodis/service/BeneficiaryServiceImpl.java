/** Service implementation for Beneficiary business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.dto.BeneficiaryDto;
import com.rex.commodis.dto.BeneficiaryFilterRequest;
import com.rex.commodis.dto.BeneficiaryPageResponse;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.mapper.BeneficiaryManualMapper;
import com.rex.commodis.model.Beneficiary;
import com.rex.commodis.repository.BeneficiaryRepository;
import com.rex.commodis.specification.BeneficiarySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeneficiaryServiceImpl implements BeneficiaryService {

        private final BeneficiaryRepository beneficiaryRepository;
        private final BeneficiaryManualMapper beneficiaryMapper;

        private static final Integer DEFAULT_PAGE_NUMBER = 0;
        private static final Integer DEFAULT_PAGE_SIZE = 10;
        private static final String DEFAULT_SORT_BY = "lastName";
        private static final String DEFAULT_SORT_DIRECTION = "asc";

        @Override
        @Transactional(readOnly = true)
        public BeneficiaryPageResponse fetchBeneficiaries(BeneficiaryFilterRequest filter) {
                int page = filter.getPage() != null ? filter.getPage() : DEFAULT_PAGE_NUMBER;
                int size = filter.getSize() != null ? filter.getSize() : DEFAULT_PAGE_SIZE;
                String sortBy = filter.getSortBy() != null ? filter.getSortBy() : DEFAULT_SORT_BY;
                Sort.Direction dir = "desc".equalsIgnoreCase(filter.getSortDirection())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sortBy));

                Specification<Beneficiary> spec = BeneficiarySpecification.getSpecifications(filter);
                Page<Beneficiary> pageResult = beneficiaryRepository.findAll(spec, pageable);

                List<BeneficiaryDto> dtos = pageResult.getContent().stream()
                        .map(beneficiaryMapper::beneficiaryToBeneficiaryDto)
                        .collect(Collectors.toList());

                return BeneficiaryPageResponse.builder()
                        .content(dtos)
                        .pageNumber(pageResult.getNumber())
                        .pageSize(pageResult.getSize())
                        .totalElements(pageResult.getTotalElements())
                        .totalPages(pageResult.getTotalPages())
                        .last(pageResult.isLast())
                        .build();
        }

        @Override
        @Transactional(readOnly = true)
        public BeneficiaryDto getBeneficiaryById(java.util.UUID id) {
                Beneficiary b = beneficiaryRepository.findByBeneficiaryId(id)
                        .orElseThrow(() -> new NotFoundException("Beneficiary not found with UUID: " + id));
                return beneficiaryMapper.beneficiaryToBeneficiaryDto(b);
        }

        @Override
        @Transactional(readOnly = true)
        public List<BeneficiaryDto> searchBeneficiariesByPlatform(Long platformId) {
                return beneficiaryRepository.selectBeneficiariesByPlatform(platformId).stream()
                        .map(row -> BeneficiaryDto.builder()
                                .id(((Number) row[0]).longValue())
                                .listName((String) row[1])
                                .identityTag((String) row[2])
                                .employerName((String) row[3])
                                .orgUnit((String) row[4])
                                .costCenter((String) row[5])
                                .build())
                        .collect(Collectors.toList());
        }
}
