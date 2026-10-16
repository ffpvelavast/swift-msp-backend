/** Service interface defining DeputyRight operations; implemented by DeputyRightServiceImpl. */
package com.rex.commodis.service;

import java.util.List;

import com.rex.commodis.dto.DeputyRightDto;

/**
 * Service interface for managing DeputyRight entities.
 */
public interface DeputyRightService {

    /**
     * Fetch all deputy rights.
     * @return list of DeputyRightDto
     */
    List<DeputyRightDto> fetchAll();

    /**
     * Fetch a single deputy right by its ID.
     * @param id the right ID
     * @return the DeputyRightDto
     */
    DeputyRightDto getById(Long id);

    /**
     * Create a new deputy right.
     * @param dto data for the new right
     * @return the created DeputyRightDto
     */
    DeputyRightDto create(DeputyRightDto dto);

    /**
     * Update an existing deputy right.
     * @param id the ID of the right to update
     * @param dto updated data
     * @return the updated DeputyRightDto
     */
    DeputyRightDto update(Long id, DeputyRightDto dto);

    /**
     * Delete a deputy right.
     * @param id the ID of the right to delete
     */
    void delete(Long id);
}
