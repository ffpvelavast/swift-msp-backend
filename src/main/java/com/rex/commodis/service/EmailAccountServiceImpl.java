/** Service implementation for EmailAccount business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.EmailAccount;
import com.rex.commodis.repository.EmailAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailAccountServiceImpl implements EmailAccountService {

    private final EmailAccountRepository emailAccountRepository;

    @Override
    @Transactional(readOnly = true)
    public EmailAccount getEmailAccount(Long id) {
        return emailAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Email account not found with id: " + id
                ));
    }
}
