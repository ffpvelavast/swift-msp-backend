/** MapStruct mapper converting between BeneficiaryManual entities and DTOs. */
package com.rex.commodis.mapper;

import com.rex.commodis.dto.BeneficiaryDto;
import com.rex.commodis.model.Beneficiary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BeneficiaryManualMapper {

    public BeneficiaryDto beneficiaryToBeneficiaryDto(Beneficiary b) {
        if (b == null) {
            return null;
        }

        return BeneficiaryDto.builder()
                .id(b.getId())
                .beneficiaryId(b.getBeneficiaryId())
                .commodisId(b.getCommodisId())
                .lastName(b.getLastName())
                .firstName(b.getFirstName())
                .academicTitle(b.getAcademicTitle())
                .listName(b.getListName())
                .leavingDate(b.getLeavingDate())
                .createdAt(b.getCreatedAt())
                .updatedAt(b.getUpdatedAt())
                .build();
    }

    public Beneficiary beneficiaryDtoToBeneficiary(BeneficiaryDto dto) {
        if (dto == null) {
            return null;
        }

        return Beneficiary.builder()
                .id(dto.getId())
                .beneficiaryId(dto.getBeneficiaryId())
                .commodisId(dto.getCommodisId())
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .academicTitle(dto.getAcademicTitle())
                .listName(dto.getListName())
                .leavingDate(dto.getLeavingDate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public List<BeneficiaryDto> beneficiariesToBeneficiaryDtos(List<Beneficiary> list) {
        if (list == null) {
            return null;
        }
        return list.stream()
                .map(this::beneficiaryToBeneficiaryDto)
                .collect(Collectors.toList());
    }
}
