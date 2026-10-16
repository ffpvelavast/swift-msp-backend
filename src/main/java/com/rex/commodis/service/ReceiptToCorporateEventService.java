/** Service interface defining ReceiptToCorporateEvent operations; implemented by ReceiptToCorporateEventServiceImpl. */
package com.rex.commodis.service;

public interface ReceiptToCorporateEventService {

    void saveCorporateEvent(Long receiptId, Long corporateEventId);
}
