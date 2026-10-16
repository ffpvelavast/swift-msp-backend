/** Service interface defining DeputyAssignment operations; implemented by DeputyAssignmentServiceImpl. */


package com.rex.commodis.service;

import com.rex.commodis.dto.DeputyAssignmentDto;
import java.util.List;

/**
 * Service interface for managing DeputyAssignment entities.
 */
public interface DeputyAssignmentService {

    /**
     * Fetch all assignments for a given rexuser account.
     * 
     * @param rexuserId the ID of the rexuser account
     * @return list of DeputyAssignmentDto
     */
    List<DeputyAssignmentDto> fetchByRexuserId(Long rexuserId);

    /**
     * Create a new deputy assignment.
     *
     * @param dto data for the new assignment
     * @return the created DeputyAssignmentDto
     */
    DeputyAssignmentDto create(DeputyAssignmentDto dto);

    /**
     * Update an existing deputy assignment.
     *
     * @param id  the ID of the assignment to update
     * @param dto updated data
     * @return the updated DeputyAssignmentDto
     */
    DeputyAssignmentDto update(Long id, DeputyAssignmentDto dto);

    /**
     * Delete a deputy assignment.
     *
     * @param id the ID of the assignment to delete
     */
    void delete(Long id);
}
