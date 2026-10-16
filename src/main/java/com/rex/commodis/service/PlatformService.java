/** Service interface defining Platform operations; implemented by PlatformServiceImpl. */
package com.rex.commodis.service;

import java.util.List;

import com.rex.commodis.dto.PlatformDto;

/**
 * Service interface for managing Platform entities.
 */
public interface PlatformService {

    /**
     * Fetch all platforms.
     * @return list of PlatformDto
     */
    List<PlatformDto> fetchAll();

    /**
     * Fetch a single platform by its ID.
     * @param id the platform ID
     * @return the PlatformDto
     */
    PlatformDto getById(Long id);
    /**
     * Fetch all platforms.
     * @return list of PlatformDto
     */
    public PlatformDto getByDisplayname(String name);

    /**
     * Create a new platform.
     * @param dto data for the new platform
     * @return the created PlatformDto
     */
    PlatformDto create(PlatformDto dto);

    /**
     * Update an existing platform.
     * @param id the ID of the platform to update
     * @param dto updated data
     * @return the updated PlatformDto
     */
    PlatformDto update(Long id, PlatformDto dto);

    /**
     * Delete a platform.
     * @param id the ID of the platform to delete
     */
    void delete(Long id);
}
