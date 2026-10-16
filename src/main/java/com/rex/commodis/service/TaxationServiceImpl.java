/** Service implementation for Taxation business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.ExpenseCategoryDropDownDto;
import com.rex.commodis.dto.receipts.ExpenseReasonDropDownDto;
import com.rex.commodis.repository.taxation.TaxCategoryRepository;
import com.rex.commodis.repository.taxation.TaxReasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxationServiceImpl implements TaxationService{

    private final TaxReasonRepository taxReasonRepository;
    private final TaxCategoryRepository taxCategoryRepository;

    public List<ExpenseCategoryDropDownDto> getTaxCategories(Long platformId, String locale) {
        return taxCategoryRepository.findTaxCategories(platformId, locale)
                .stream()
                .filter(taxCategory ->
                        StringUtils.hasText(taxCategory.getDisplaytext()))
                .map(taxCategory -> new ExpenseCategoryDropDownDto(
                        taxCategory.getTaxCategoryId(),
                        taxCategory.getDisplaytext(),
                        taxCategory.getDescription()
                )).toList();
    }

    public List<ExpenseReasonDropDownDto> getTaxReasons(Long platformId, Long taxCategoryId, String locale){

        boolean isValid = taxCategoryRepository.existsCategoryForPlatform(taxCategoryId, platformId);

        if(!isValid){
            throw new AccessDeniedException("User cannot access reasons for given tax category");
        }

        //Run function select_taxreason using selected taxCategoryId from first dropdown,
        //map output of query to projection, map output of projection to DTO
        return taxReasonRepository.findTaxReasons(taxCategoryId,locale)
                .stream()
                .filter(taxReason ->
                        StringUtils.hasText(taxReason.getDisplaytext()))
                .map(taxReason -> new ExpenseReasonDropDownDto(
                        taxReason.getTaxReasonId(),
                        taxReason.getDisplaytext(),
                        taxReason.getDescription()
                ))
                .toList();

    }
}
