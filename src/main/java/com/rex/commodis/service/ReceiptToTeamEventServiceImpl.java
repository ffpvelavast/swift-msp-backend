/** Service implementation for ReceiptToTeamEvent business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.model.receipts.receiptToTeamEvent.ReceiptToTeamEvent;
import com.rex.commodis.repository.receipts.ReceiptToTeamEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptToTeamEventServiceImpl implements ReceiptToTeamEventService{

    private final ReceiptToTeamEventRepository receiptToTeamEventRepository;

    public void saveTeamEvent(Long receiptId, Long teamEventId) {

        ReceiptToTeamEvent receiptToTeamEvent = ReceiptToTeamEvent.builder()
                .receiptId(receiptId)
                .teamEventId(teamEventId)
                .build();

        receiptToTeamEventRepository.save(receiptToTeamEvent);
    }
}
