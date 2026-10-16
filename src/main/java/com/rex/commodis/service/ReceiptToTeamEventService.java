/** Service interface defining ReceiptToTeamEvent operations; implemented by ReceiptToTeamEventServiceImpl. */
package com.rex.commodis.service;

public interface ReceiptToTeamEventService {

    void saveTeamEvent(Long receiptId, Long teamEventId);
}
