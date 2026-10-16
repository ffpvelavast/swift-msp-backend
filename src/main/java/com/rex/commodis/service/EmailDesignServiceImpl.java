/** Service implementation for EmailDesign business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.EmailDesign;
import com.rex.commodis.repository.EmailDesignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailDesignServiceImpl implements EmailDesignService {

    private final EmailDesignRepository emailDesignRepository;

    @Override
    @Transactional(readOnly = true)
    public EmailDesign getEmailDesign(Long id) {
        return emailDesignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Email design not found with id: " + id
                ));
    }
}
