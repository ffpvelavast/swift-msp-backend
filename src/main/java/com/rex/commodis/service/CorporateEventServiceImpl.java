/** Service implementation for CorporateEvent business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.dto.receipts.ReceiptEventRequestDto;
import com.rex.commodis.model.events.CorporateEvent;
import com.rex.commodis.repository.events.CorporateEventRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CorporateEventServiceImpl implements CorporateEventService{

    private final CorporateEventRepository corporateEventRepository;

    public CorporateEvent getCorporateEvent(ReceiptEventRequestDto eventDto){

        CorporateEvent corporateEvent = corporateEventRepository
                .findById(eventDto.getEventId())
                .orElseThrow();
        return corporateEvent;
    }
}