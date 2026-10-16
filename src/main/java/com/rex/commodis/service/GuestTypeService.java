/** Service interface defining GuestType operations; implemented by GuestTypeServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.GuestTypeDto;
import java.util.List;

/**
 * Service interface for managing GuestType entities.
 */
public interface GuestTypeService {

    /**
     * Fetch all guest types.
     * @return list of GuestTypeDto
     */
    List<GuestTypeDto> fetchAll(String locale);

    /**
     * Fetch a single guest type by its ID.
     * @param id the guest type ID
     * @return the GuestTypeDto
     */
    GuestTypeDto getById(Long id);

    /**
     * Create a new guest type.
     * @param dto data for the new guest type
     * @return the created GuestTypeDto
     */
    GuestTypeDto create(GuestTypeDto dto);

    /**
     * Update an existing guest type.
     * @param id the ID of the guest type to update
     * @param dto updated data
     * @return the updated GuestTypeDto
     */
    GuestTypeDto update(Long id, GuestTypeDto dto);

    /**
     * Delete a guest type.
     * @param id the ID of the guest type to delete
     */
    void delete(Long id);
}