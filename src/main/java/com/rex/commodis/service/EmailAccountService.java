/** Service interface defining EmailAccount operations; implemented by EmailAccountServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.model.EmailAccount;

public interface EmailAccountService {

    EmailAccount getEmailAccount(Long id);
}
