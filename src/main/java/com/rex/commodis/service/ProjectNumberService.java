/** Service interface defining ProjectNumber operations; implemented by ProjectNumberServiceImpl. */
package com.rex.commodis.service;

import java.util.List;
import java.util.UUID;

import com.rex.commodis.dto.ProjectNumberSearchResponseDto;
import com.rex.commodis.dto.projectNumberDtos.request.CreateProjectNumberRequestDto;
import com.rex.commodis.dto.projectNumberDtos.request.UpdateProjectNumberRequestDto;
import com.rex.commodis.dto.projectNumberDtos.response.CreateProjectNumberResponseDto;
import com.rex.commodis.dto.projectNumberDtos.response.GetProjectNumberPageResponseDto;
import com.rex.commodis.dto.projectNumberDtos.response.GetProjectNumberResponseDto;
import com.rex.commodis.dto.projectNumberDtos.response.UpdateProjectNumberResponseDto;

public interface ProjectNumberService {

    CreateProjectNumberResponseDto createProjectNumbers(
            Long platformId,
            CreateProjectNumberRequestDto createProjectNumberRequestDto
    );

    GetProjectNumberPageResponseDto getAllProjectNumbers(Long platformId, String projectNumber, int page, int size);

    GetProjectNumberResponseDto getProjectNumberById(Long platformId, UUID projectNumberId);

    void deleteProjectNumber(Long platformId, UUID projectNumberId);

    void deleteProjectNumberByNumber(Long platformId, String projectNumber);

    UpdateProjectNumberResponseDto updateByProjectNumber(
            Long platformId,
            UpdateProjectNumberRequestDto updateProjectNumberRequestDto
    );

    UpdateProjectNumberResponseDto updateByProjectNumberId(
            Long platformId,
            UUID projectNumberId,
            UpdateProjectNumberRequestDto updateProjectNumberRequestDto
    );

    Long resolveProjectNumberId(String documentId, String projectNumber);

    List<ProjectNumberSearchResponseDto> searchProjectNumbers(Long platformId, String searchText, Integer limit);
}
