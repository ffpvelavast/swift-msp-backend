/** Service interface defining PlatformConfiguration operations; implemented by PlatformConfigurationServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.model.PlatformConfiguration;

public interface PlatformConfigurationService {
    Long getPlatformIdByRealm(String realm);
    PlatformConfiguration getActiveConfigByPlatformId(Long platformId);
    String getReceiptStorageBaseUrl(Long platformId);

}