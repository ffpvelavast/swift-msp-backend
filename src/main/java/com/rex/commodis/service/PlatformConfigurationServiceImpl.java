/** Service implementation for PlatformConfiguration business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import com.rex.commodis.exceptions.ForbiddenException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.PlatformConfiguration;
import com.rex.commodis.repository.PlatformConfigurationRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlatformConfigurationServiceImpl implements PlatformConfigurationService {

    private final PlatformConfigurationRepository repository;

    @Override
    @Transactional
    public Long getPlatformIdByRealm(String realm) {
        return repository.findByKeycloakRealm(realm)
                .map(pc -> pc.getPlatform().getId())
                .orElseThrow(() -> new ForbiddenException("Invalid realm: " + realm));
    }

    @Override
    @Transactional(readOnly = true)
    public PlatformConfiguration getActiveConfigByPlatformId(Long platformId) {
        return repository.findActiveByPlatformId(platformId)
                .orElseThrow(() -> new NotFoundException("No active platform configuration for platformId: " + platformId));
    }

    @Override
    public String getReceiptStorageBaseUrl(Long platformId) {
        PlatformConfiguration config = getActiveConfigByPlatformId(platformId);

        if (config.getReceiptStorageLink() == null) {
            throw new BadRequestException("Receipt storage base URL not configured for platform: " + platformId);
        }

        return config.getReceiptStorageLink();
    }
}