/** BeneficiarySpecification: part of the REX multi-tenant expense management platform. */
package com.rex.commodis.specification;

import jakarta.persistence.criteria.Predicate;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import com.rex.commodis.dto.BeneficiaryFilterRequest;
import com.rex.commodis.model.Beneficiary;

import java.util.ArrayList;
import java.util.List;

public class BeneficiarySpecification {

    private BeneficiarySpecification() {
        /* utility */ }

    public static Specification<Beneficiary> getSpecifications(BeneficiaryFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();

            if (filter.getBeneficiaryUuid() != null) {
                preds.add(cb.equal(root.get("beneficiaryId"), filter.getBeneficiaryUuid()));
            }

            if (Strings.isNotEmpty(filter.getLastName())) {
                preds.add(cb.like(
                        cb.lower(root.get("lastName")),
                        "%" + filter.getLastName().toLowerCase() + "%"));
            }

            if (Strings.isNotEmpty(filter.getFirstName())) {
                preds.add(cb.like(
                        cb.lower(root.get("firstName")),
                        "%" + filter.getFirstName().toLowerCase() + "%"));
            }

            if (Strings.isNotEmpty(filter.getAcademicTitle())) {
                preds.add(cb.like(
                        cb.lower(root.get("academicTitle")),
                        "%" + filter.getAcademicTitle().toLowerCase() + "%"));
            }

            if (Strings.isNotEmpty(filter.getListName())) {
                preds.add(cb.like(
                        cb.lower(root.get("listName")),
                        "%" + filter.getListName().toLowerCase() + "%"));
            }

            return cb.and(preds.toArray(new Predicate[0]));
        };
    }
}
