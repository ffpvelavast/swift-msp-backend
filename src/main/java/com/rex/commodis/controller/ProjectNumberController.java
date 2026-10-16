/** REST controller exposing ProjectNumber API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rex.commodis.dto.ProjectNumberSearchResponseDto;
import com.rex.commodis.dto.projectNumberDtos.request.CreateProjectNumberRequestDto;
import com.rex.commodis.dto.projectNumberDtos.request.DeleteProjectNumberRequestDto;
import com.rex.commodis.dto.projectNumberDtos.request.UpdateProjectNumberRequestDto;
import com.rex.commodis.dto.projectNumberDtos.response.CreateProjectNumberResponseDto;
import com.rex.commodis.dto.projectNumberDtos.response.GetProjectNumberPageResponseDto;
import com.rex.commodis.dto.projectNumberDtos.response.GetProjectNumberResponseDto;
import com.rex.commodis.dto.projectNumberDtos.response.UpdateProjectNumberResponseDto;
import com.rex.commodis.security.user.ClientPrincipal;
import com.rex.commodis.security.user.RexUserContext;
import com.rex.commodis.service.ProjectNumberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/projectnumbers")
@RequiredArgsConstructor
public class ProjectNumberController {

    private final ProjectNumberService projectNumberService;

    /** POST /projectnumbers */
    @PostMapping
    public ResponseEntity<CreateProjectNumberResponseDto> createProjectNumber(
            @Valid @RequestBody CreateProjectNumberRequestDto createProjectNumberRequestDto,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ){
        Long platformId = rexUserContext.getPlatformId();

        CreateProjectNumberResponseDto createProjectNumberResponseDto = projectNumberService.createProjectNumbers(
                platformId,
                createProjectNumberRequestDto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createProjectNumberResponseDto);
    }


    /** GET /projectnumbers?page=0&size=20 */
    @GetMapping
    public ResponseEntity<GetProjectNumberPageResponseDto> getAllProjectNumbers(
            @RequestParam(required = false) String projectNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ){
        Long platformId = rexUserContext.getPlatformId();

        GetProjectNumberPageResponseDto getProjectNumberPageResponseDto =
                projectNumberService.getAllProjectNumbers(platformId, projectNumber, page, size);

        return ResponseEntity.ok(getProjectNumberPageResponseDto);
    }

    /** GET /projectnumbers/by-id/{projectNumberId} */
    @GetMapping("/by-id/{projectNumberId}")
    public ResponseEntity<GetProjectNumberResponseDto> getProjectNumberById(
            @PathVariable("projectNumberId") UUID projectNumberId,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ){
        Long platformId = rexUserContext.getPlatformId();

        GetProjectNumberResponseDto getProjectNumberResponseDto =
                projectNumberService.getProjectNumberById(platformId, projectNumberId);

        return ResponseEntity.ok(getProjectNumberResponseDto);
    }

    /** DELETE /projectnumbers/by-id/{projectNumberId} */
    @DeleteMapping("/by-id/{projectNumberId}")
    public ResponseEntity<Void> deleteProjectNumber(@PathVariable UUID projectNumberId,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ){
        Long platformId = rexUserContext.getPlatformId();

        projectNumberService.deleteProjectNumber(platformId, projectNumberId);

        return ResponseEntity.noContent().build(); // 204
    }

    /** DELETE /projectnumbers/by-number */
    @DeleteMapping("/by-number")
    public ResponseEntity<Void> deleteProjectNumberByNumber(
            @Valid @RequestBody DeleteProjectNumberRequestDto deleteProjectNumberRequestDto,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ){
        Long platformId = rexUserContext.getPlatformId();

        projectNumberService.deleteProjectNumberByNumber(
                platformId,
                deleteProjectNumberRequestDto.getProjectNumber()
        );

        return ResponseEntity.noContent().build(); // 204
    }

    /** PATCH /projectnumbers/by-number*/
    @PatchMapping("/by-number")
    public ResponseEntity<UpdateProjectNumberResponseDto> updateProjectByNumber(
            @Valid @RequestBody UpdateProjectNumberRequestDto updateProjectNumberRequestDto,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ){
        Long platformId = rexUserContext.getPlatformId();

        UpdateProjectNumberResponseDto updateProjectNumberResponseDto =
                projectNumberService.updateByProjectNumber(
                        platformId,
                        updateProjectNumberRequestDto
                );

        return ResponseEntity.ok(updateProjectNumberResponseDto);
    }

    /** PATCH /projectnumbers/by-id/{projectNumberId} */
    @PatchMapping("/by-id/{projectNumberId}")
    public ResponseEntity<UpdateProjectNumberResponseDto> updateByProjectNumberId(
            @PathVariable UUID projectNumberId,
            @Valid @RequestBody UpdateProjectNumberRequestDto updateProjectNumberRequestDto,
            @AuthenticationPrincipal ClientPrincipal clientPrincipal,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ){
        Long platformId = rexUserContext.getPlatformId();

        UpdateProjectNumberResponseDto updateProjectNumberResponseDto =
                projectNumberService.updateByProjectNumberId(platformId, projectNumberId, updateProjectNumberRequestDto);

        return ResponseEntity.ok(updateProjectNumberResponseDto);
    }

    /** GET /projectnumbers/search*/
    @GetMapping("/search")
    public ResponseEntity<List<ProjectNumberSearchResponseDto>> searchProjectNumbers(
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false, defaultValue = "50") Integer limit,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

    	Long platformId = rexUserContext.getPlatformId();

        return ResponseEntity.ok(
                projectNumberService.searchProjectNumbers(platformId, searchText, limit)
        );
    }
}
