/** Service interface defining Taxation operations; implemented by TaxationServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.ExpenseCategoryDropDownDto;
import com.rex.commodis.dto.receipts.ExpenseReasonDropDownDto;

import java.util.List;

public interface TaxationService {

    List<ExpenseReasonDropDownDto> getTaxReasons(Long platformId, Long taxCategoryId, String locale);

    List<ExpenseCategoryDropDownDto> getTaxCategories(Long platformId, String locale);
}
