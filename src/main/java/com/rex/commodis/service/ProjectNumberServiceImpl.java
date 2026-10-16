/** Service implementation for ProjectNumber business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


import com.rex.commodis.dto.ProjectNumberSearchResponseDto;
import com.rex.commodis.dto.projectNumberDtos.ProjectLeaderSummaryDto;
import com.rex.commodis.dto.projectNumberDtos.ProjectNumberBaseDto;
import com.rex.commodis.dto.projectNumberDtos.request.CreateProjectNumberRequestDto;
import com.rex.commodis.dto.projectNumberDtos.request.UpdateProjectNumberRequestDto;
import com.rex.commodis.dto.projectNumberDtos.response.CreateProjectNumberResponseDto;
import com.rex.commodis.dto.projectNumberDtos.response.GetProjectNumberPageResponseDto;
import com.rex.commodis.dto.projectNumberDtos.response.GetProjectNumberResponseDto;
import com.rex.commodis.dto.projectNumberDtos.response.UpdateProjectNumberResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.ConflictException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.mapper.ProjectNumberMapper;
import com.rex.commodis.mapper.ProjectNumberSearchProjection;
import com.rex.commodis.model.ProjectLeader;
import com.rex.commodis.model.ProjectNumber;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.repository.PlatformRepository;
import com.rex.commodis.repository.ProjectLeaderRepository;
import com.rex.commodis.repository.ProjectNumberRepository;
import com.rex.commodis.repository.RexUserRepository;
import com.rex.commodis.repository.invoices.InvoicePositionRepository;
import com.rex.commodis.repository.receipts.ReceiptRepository;
import com.rex.commodis.service.dvelopServices.InvoiceAssignmentMailService;
import com.rex.commodis.service.invoiceServices.InvoiceErrorService;
import com.rex.commodis.service.invoiceServices.InvoicePositionService;
import com.rex.commodis.service.invoiceServices.InvoicesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectNumberServiceImpl implements ProjectNumberService {

    private final InvoiceErrorService invoiceErrorService;
    private final InvoiceAssignmentMailService invoiceAssignmentMailService;
    private final ReceiptRepository receiptRepository;
    private final ReceiptService receiptService;
    private final TeamEventService teamEventService;
    private final InvoicesService invoicesService;
    private final InvoicePositionService invoicePositionService;
    private final RexUserRepository rexUserRepository;
    private final ProjectNumberRepository projectNumberRepository;
    private final ProjectLeaderRepository projectLeaderRepository;
    private final ProjectNumberMapper projectNumberMapper;

    //private final InvoiceRepository invoiceRepository;
    private final PlatformRepository platformRepository;

    private static final LocalDate MAX_DATE = LocalDate.of(2999, 12, 31);

    @Transactional
    public CreateProjectNumberResponseDto createProjectNumbers(Long platformId, CreateProjectNumberRequestDto createProjectNumberRequestDto) {

        //Check if projectNumber already exists by composite PK (PlatformId & ProjectNumber)
        Optional<ProjectNumber> existingProjectNumber = projectNumberRepository
                .findByPlatformIdAndProjectNumber(platformId, createProjectNumberRequestDto.getProjectNumber());

        if (existingProjectNumber.isPresent()) {
            throw new ConflictException(
                    "Project Number already exists: " + createProjectNumberRequestDto.getProjectNumber()
            );
        }

        //If not already existing, then map Dto to entity and save to DB
        ProjectNumber projectNumber = requestToProjectNumberEntity(createProjectNumberRequestDto, platformId);
        ProjectNumber savedProjectNumber = projectNumberRepository.save(projectNumber);

        //Collect emails from ProjectLeaders
        List<String> normalizedEmails = createProjectNumberRequestDto.getProjectLeaders().stream()
                .map(ProjectLeaderSummaryDto::getEmail)
                .filter(Objects::nonNull)
                .map(email -> email.trim().toLowerCase())
                .collect(Collectors.toList());

        //Find RexUsers by email & platformII
        String[] emailArray = normalizedEmails.toArray(new String[0]);

        List<RexUser> dbRexUsers =
                rexUserRepository.findRexUsersByEmailIdsAndPlatformId(emailArray, platformId);

        //Create lookup map
        Map<String, RexUser> dbRexUserMap = dbRexUsers.stream()
                .collect(Collectors.toMap(
                        user -> user.getEmail().toLowerCase(),
                        user -> user
                ));

        //Identify invalid emails
        List<String> invalidEmails = normalizedEmails.stream()
                .filter(email -> !dbRexUserMap.containsKey(email))
                .toList();

        //Save Project Leader information to DB
        List<ProjectLeader> projectLeaders = new ArrayList<>();
        for( ProjectLeaderSummaryDto projectLeaderSummaryDto : createProjectNumberRequestDto.getProjectLeaders()){

            String normalizedEmail = projectLeaderSummaryDto.getEmail().trim().toLowerCase();

            RexUser dbRexUser = dbRexUserMap.get(normalizedEmail);

            if(dbRexUser == null)
                continue;

            ProjectLeader projectLeader = new ProjectLeader();
            //Newly generated ProjectNumbers.Id servers as FK here
            projectLeader.setProjectNumberId(savedProjectNumber.getId());
            projectLeader.setRexUserId(dbRexUser.getId());
            projectLeader.setRexUser(dbRexUser);
            projectLeader.setActiveFrom(projectLeaderSummaryDto.getActiveFrom());

            projectLeader.setActiveThrough(
                    projectLeaderSummaryDto.getActiveThrough() != null
                            ? projectLeaderSummaryDto.getActiveThrough()
                            : MAX_DATE
            );

            projectLeaders.add(projectLeader);
        }

        //Save all project leaders and attach to entity
        List<ProjectLeader> savedLeaders = projectLeaderRepository.saveAll(projectLeaders);
        savedProjectNumber.setProjectLeaders(savedLeaders);

        //Build response object
        ProjectNumberBaseDto baseDto = toProjectNumberBaseDto(savedProjectNumber);
        CreateProjectNumberResponseDto createProjectNumberResponseDto = new CreateProjectNumberResponseDto(baseDto, invalidEmails);

        return createProjectNumberResponseDto;
    }

    @Transactional
    public GetProjectNumberPageResponseDto getAllProjectNumbers(Long platformId, String projectNumber,
                                                                int page, int size){

        //Run pagination query based on input filters
        Pageable pageable = PageRequest.of(page, size);

        if (projectNumber != null && projectNumber.isBlank()) {
            throw new BadRequestException("Project Number cannot be blank");
        }

        Page<ProjectNumber> pageResult =
                (projectNumber != null)
                        ? projectNumberRepository.findByPlatformIdAndProjectNumber(platformId, projectNumber, pageable)
                        : projectNumberRepository.findByPlatformId(platformId, pageable);

        //Map Entity classes to DTOs
        List<ProjectNumberBaseDto> projectNumberDtos = pageResult.getContent()
                .stream()
                .map(this::toProjectNumberBaseDto)
                .toList();

        //Construct response object
        GetProjectNumberPageResponseDto getProjectNumberPageResponseDto = new GetProjectNumberPageResponseDto();
        getProjectNumberPageResponseDto.setContent(projectNumberDtos);
        getProjectNumberPageResponseDto.setPage(page);
        getProjectNumberPageResponseDto.setSize(size);
        getProjectNumberPageResponseDto.setTotalElements(pageResult.getTotalElements());
        getProjectNumberPageResponseDto.setTotalPages(pageResult.getTotalPages());

        return getProjectNumberPageResponseDto;
    }

    @Transactional
    public GetProjectNumberResponseDto getProjectNumberById(Long platformId, UUID projectNumberId) {

        ProjectNumber entity = projectNumberRepository
                .findByPlatformIdAndProjectNumberId(platformId, projectNumberId)
                .orElseThrow(() ->
                        new NotFoundException("Project Number not found")
                );

        ProjectNumberBaseDto projectNumberBaseDto = toProjectNumberBaseDto(entity);

        return new GetProjectNumberResponseDto(projectNumberBaseDto);
    }

    @Transactional
    public void deleteProjectNumber(Long platformId, UUID projectNumberId) {

        ProjectNumber projectNumber = projectNumberRepository
                .findByPlatformIdAndProjectNumberId(platformId, projectNumberId)
                .orElseThrow(() ->
                        new NotFoundException("Project Number not found")
                );
    /*
        boolean hasReferences = invoiceRepository.existsByProjectNumberId(projectNumber.getId());
        if (hasReferences) {
            throw new ConflictException("Cannot delete: Project Number references exist in invoices");
        }

     */
        existsInReceipts(projectNumber.getId());
        existsInTeamEvents(projectNumber.getId());
        existsInInvoices(projectNumber.getId());
        existsInInvoicePositions(projectNumber.getId());

        projectLeaderRepository.deleteByProjectNumberId(projectNumber.getId());
        projectNumberRepository.delete(projectNumber);
    }

    @Transactional
    public void deleteProjectNumberByNumber(Long platformId, String projectNumber) {

        ProjectNumber projectNumberEntity = projectNumberRepository
                .findByPlatformIdAndProjectNumber(platformId, projectNumber)
                .orElseThrow(() ->
                        new NotFoundException("Project Number not found")
                );
       /*
        boolean hasReferences = invoiceRepository.existsByProjectNumberId(projectNumberEntity.getId());
        if (hasReferences) {
            throw new ConflictException("Cannot delete: Project Number references exist in invoices");
        }
        */

        existsInReceipts(projectNumberEntity.getId());
        existsInTeamEvents(projectNumberEntity.getId());
        existsInInvoices(projectNumberEntity.getId());
        existsInInvoicePositions(projectNumberEntity.getId());


        projectLeaderRepository.deleteByProjectNumberId(projectNumberEntity.getId());
        projectNumberRepository.delete(projectNumberEntity);
    }

    @Transactional
    public UpdateProjectNumberResponseDto updateByProjectNumber(
            Long platformId,
            UpdateProjectNumberRequestDto updateProjectNumberRequestDto) {

        ProjectNumber dbProjectNumber = projectNumberRepository
                .findByPlatformIdAndProjectNumber(platformId, updateProjectNumberRequestDto.getProjectNumber())
                .orElseThrow(() -> new NotFoundException("Project Number not found"));

        return updateProjectNumber(platformId, dbProjectNumber, updateProjectNumberRequestDto);
    }

    @Transactional
    public UpdateProjectNumberResponseDto updateByProjectNumberId(
            Long platformId,
            UUID projectNumberId,
            UpdateProjectNumberRequestDto updateProjectNumberRequestDto) {

        ProjectNumber dbProjectNumber = projectNumberRepository
                .findByPlatformIdAndProjectNumberId(platformId, projectNumberId)
                .orElseThrow(() -> new NotFoundException("Project Number not found"));

        return updateProjectNumber(platformId, dbProjectNumber, updateProjectNumberRequestDto);
    }

    public UpdateProjectNumberResponseDto updateProjectNumber(
            Long platformId,
            ProjectNumber dbProjectNumber,
            UpdateProjectNumberRequestDto updateProjectNumberRequestDto) {

        // UPDATE PROJECT DETAILS
        if (updateProjectNumberRequestDto.getProjectTitle() != null) {
        	dbProjectNumber.setProjectTitle(updateProjectNumberRequestDto.getProjectTitle());
        }
        if (updateProjectNumberRequestDto.getProjectSubtitle() != null) {
        	dbProjectNumber.setProjectSubtitle(updateProjectNumberRequestDto.getProjectSubtitle());
        }
        if (updateProjectNumberRequestDto.getDescription() != null) {
        	dbProjectNumber.setDescription(updateProjectNumberRequestDto.getDescription());
        }
        if (updateProjectNumberRequestDto.getActiveFrom() != null) {
        	dbProjectNumber.setActiveFrom(updateProjectNumberRequestDto.getActiveFrom());
        }
        if (updateProjectNumberRequestDto.getActiveThrough() != null) {
            dbProjectNumber.setActiveThrough(updateProjectNumberRequestDto.getActiveThrough());
        }

        ProjectNumber updatedDbProjectNumber = projectNumberRepository.save(dbProjectNumber);

        List<String> invalidRequestEmails = new ArrayList<>();

        // PROCESS PROJECT LEADERS (MERGE LOGIC)
        if (updateProjectNumberRequestDto.getProjectLeaders() != null) {

            List<ProjectLeader> requestProjectLeaders = new ArrayList<>();
            Set<Long> requestRexUserIds = new HashSet<>();

            for (ProjectLeaderSummaryDto projectLeaderDto : updateProjectNumberRequestDto.getProjectLeaders()) {

                String normalizedEmail = projectLeaderDto.getEmail().trim().toLowerCase();

                Optional<RexUser> dbRexUserOptional =
                        rexUserRepository.findByEmailIgnoreCaseAndPlatformIdAndActiveTrue(normalizedEmail, platformId);

                if (dbRexUserOptional.isEmpty()){
                	invalidRequestEmails.add(normalizedEmail);
                    continue;
                }

                RexUser dbRexUser = dbRexUserOptional.get();
                requestRexUserIds.add(dbRexUser.getId());

                Optional<ProjectLeader> dbExistingProjectLeader =
                        projectLeaderRepository.findByProjectNumberIdAndRexUserId(
                        		dbProjectNumber.getId(),
                                dbRexUser.getId()
                        );

                if (dbExistingProjectLeader.isPresent()) {

                    ProjectLeader dbProjectLeaderExisting = dbExistingProjectLeader.get();

                    if (projectLeaderDto.getActiveFrom() != null) {
                    	dbProjectLeaderExisting.setActiveFrom(projectLeaderDto.getActiveFrom());
                    }
                    if (projectLeaderDto.getActiveThrough() != null) {
                    	dbProjectLeaderExisting.setActiveThrough(projectLeaderDto.getActiveThrough());
                    }

                    ProjectLeader dbProjectLeader = projectLeaderRepository.save(dbProjectLeaderExisting);
                    dbProjectLeader.setRexUser(dbRexUser);

                    requestProjectLeaders.add(dbProjectLeader);

                } else {

                    ProjectLeader projectLeader = new ProjectLeader();
                    projectLeader.setProjectNumberId(dbProjectNumber.getId());
                    projectLeader.setRexUserId(dbRexUser.getId());
                    projectLeader.setActiveFrom(projectLeaderDto.getActiveFrom());
                    projectLeader.setActiveThrough(projectLeaderDto.getActiveThrough());

                    ProjectLeader dbProjectLeader = projectLeaderRepository.save(projectLeader);
                    dbProjectLeader.setRexUser(dbRexUser);

                    requestProjectLeaders.add(dbProjectLeader);
                }
            }

            // DELETE USERS NOT IN REQUEST
            List<ProjectLeader> dbExistingProjectLeaders =
                    projectLeaderRepository.findByProjectNumberId(dbProjectNumber.getId());

            for (ProjectLeader dbExistingProjectLeader : dbExistingProjectLeaders) {
                if (!requestRexUserIds.contains(dbExistingProjectLeader.getRexUserId())) {
                    projectLeaderRepository.delete(dbExistingProjectLeader);
                }
            }

            updatedDbProjectNumber.setProjectLeaders(requestProjectLeaders);
        }

        // RESPONSE
        ProjectNumberBaseDto projectNumberBaseDto = toProjectNumberBaseDto(updatedDbProjectNumber);

        return new UpdateProjectNumberResponseDto(projectNumberBaseDto, invalidRequestEmails);
    }

    @Transactional
    public Long resolveProjectNumberId(String documentId, String projectNumber) {
        return projectNumberRepository.findByProjectNumber(projectNumber)
                .map(ProjectNumber::getId)
                .orElseGet(() -> {
                    //Log Error in DB
                    invoiceErrorService.logProjectNumberLookupError(
                            documentId,
                            projectNumber,
                            "NOT_FOUND",
                            "Project number lookup failed",
                            3
                    );

                    // Send Error Mail to Admin
                    invoiceAssignmentMailService.sendMissingProjectNumberEmail(projectNumber);

                    return null;
                });
    }


    //HELPER METHODS

    private ProjectNumber requestToProjectNumberEntity(
            CreateProjectNumberRequestDto createProjectNumberRequestDto,
            Long platformId){

        ProjectNumber entity = new ProjectNumber();

        entity.setProjectNumberId(UUID.randomUUID());
        entity.setPlatformId(platformId);

        entity.setProjectNumber(createProjectNumberRequestDto.getProjectNumber());
        entity.setProjectTitle(createProjectNumberRequestDto.getProjectTitle());
        entity.setProjectSubtitle(createProjectNumberRequestDto.getProjectSubtitle());
        entity.setDescription(createProjectNumberRequestDto.getDescription());
        entity.setActiveFrom(createProjectNumberRequestDto.getActiveFrom());

        // ✅ Convert null → 2999-12-31
        LocalDate activeThrough = createProjectNumberRequestDto.getActiveThrough();
        entity.setActiveThrough(activeThrough != null ? activeThrough : MAX_DATE);

        return entity;
    }


    private ProjectNumberBaseDto toProjectNumberBaseDto(ProjectNumber entity) {

        LocalDate activeThrough = entity.getActiveThrough();

        // ✅ Convert 2999-12-31 → null
        if (activeThrough != null && activeThrough.equals(LocalDate.of(2999, 12, 31))) {
            activeThrough = null;
        }

        return ProjectNumberBaseDto.builder()
                .projectNumberId(entity.getProjectNumberId())
                .projectNumber(entity.getProjectNumber())
                .projectTitle(entity.getProjectTitle())
                .projectSubtitle(entity.getProjectSubtitle())
                .description(entity.getDescription())
                .activeFrom(entity.getActiveFrom())
                .activeThrough(activeThrough)  // ✅ use transformed value
                .projectLeaders(mapProjectLeaders(entity.getProjectLeaders()))
                .build();
    }

    private List<ProjectLeaderSummaryDto> mapProjectLeaders(List<ProjectLeader> projectLeaders) {
        return projectLeaders.stream()
                .map(projectLeader -> {
                    LocalDate activeThrough = projectLeader.getActiveThrough();

                    // ✅ Convert 2999-12-31 → null
                    if (activeThrough != null && activeThrough.equals(MAX_DATE)) {
                        activeThrough = null;
                    }

                    return ProjectLeaderSummaryDto.builder()
                            .email(projectLeader.getRexUser().getEmail())
                            .activeFrom(projectLeader.getActiveFrom())
                            .activeThrough(activeThrough) // use transformed value
                            .build();
                })
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProjectNumberSearchResponseDto> searchProjectNumbers(
            Long platformId,
            String searchText,
            Integer limit
    ) {

        // Validate input
        if (platformId == null) {
            throw new BadRequestException("Platform ID is required");
        }
        if (!platformRepository.existsById(platformId)) {
            throw new NotFoundException("Platform not found");
        }

        // Normalize inputs
        String safeQuery = (searchText == null || searchText.isBlank()) ? null: searchText.trim();

        int safeLimit = (limit == null || limit <= 0) ? 50 : limit;

        // Call repository (returns projection)
        return projectNumberRepository.searchProjectNumbers(platformId, safeQuery, safeLimit)
                .stream()
                .map(this::mapToProjectNumberSearchResponseDto)
                .toList();
    }

    //  Clean mapping using projection
    private ProjectNumberSearchResponseDto mapToProjectNumberSearchResponseDto(ProjectNumberSearchProjection p) {
        return ProjectNumberSearchResponseDto.builder()
                .projectNumberId(p.getProjectnumberId())
                .projectNumber(p.getProjectnumber())
                .projectTitle(p.getProjecttitle())
                .projectSubtitle(p.getProjectsubtitle())
                .build();
    }

    private void existsInReceipts(Long ProjectNumberId){

        boolean hasReferences = receiptService.existisByProjectNumber(ProjectNumberId);
        if (hasReferences) {
            throw new ConflictException("Cannot delete: Project Number references exist in Receipts");
        }

    }
    private void existsInTeamEvents(Long ProjectNumberId){

        boolean hasReferences = teamEventService.existsByProjectNumber(ProjectNumberId);
        if (hasReferences) {
            throw new ConflictException("Cannot delete: Project Number references exist in TeamEvents");
        }

    }
    private void existsInInvoices(Long ProjectNumberId){

        boolean hasReferences = invoicesService.existsByProjectNumber(ProjectNumberId);
        if (hasReferences) {
            throw new ConflictException("Cannot delete: Project Number references exist in Invoice");
        }

    }

    private void existsInInvoicePositions(Long projectNumberId) {

        boolean hasReferences = invoicePositionService.existsByProjectNumberId(projectNumberId);

        if (hasReferences) {
            throw new ConflictException(
                    "Cannot delete: Project Number is referenced in invoice positions"
            );
        }
    }


}



