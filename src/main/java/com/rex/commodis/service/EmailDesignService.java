/** Service interface defining EmailDesign operations; implemented by EmailDesignServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.model.EmailDesign;

public interface EmailDesignService {

    EmailDesign getEmailDesign(Long id);
}
