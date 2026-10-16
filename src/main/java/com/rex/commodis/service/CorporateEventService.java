/** Service interface defining CorporateEvent operations; implemented by CorporateEventServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.ReceiptEventRequestDto;
import com.rex.commodis.model.events.CorporateEvent;

public interface CorporateEventService {

    public CorporateEvent getCorporateEvent(ReceiptEventRequestDto eventDto);
}