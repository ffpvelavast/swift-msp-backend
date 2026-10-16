/** Service interface defining PlatformDomain operations; implemented by PlatformDomainServiceImpl. */
package com.rex.commodis.service;

import java.util.List;

import com.rex.commodis.dto.PlatformDomainDto;

/**
 * Service interface for managing PlatformDomain entities.
 */
public interface PlatformDomainService {

    /**
     * Fetch all domain entries for a given platform.
     * @param platformId the platform ID
     * @return list of PlatformDomainDto
     */
    List<PlatformDomainDto> fetchByPlatformId(Long platformId);

    /**
     * Create a new domain entry for a platform.
     * @param dto the domain data
     * @return the created PlatformDomainDto
     */
    PlatformDomainDto create(PlatformDomainDto dto);

    /**
     * Delete a domain entry by its ID.
     * @param id the domain entry ID
     */
    void delete(Long id);
}
