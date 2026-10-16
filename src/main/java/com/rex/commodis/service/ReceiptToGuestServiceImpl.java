/** Service implementation for ReceiptToGuest business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.model.receipts.receiptToGuest.ReceiptToGuest;
import com.rex.commodis.repository.receipts.ReceiptToGuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptToGuestServiceImpl implements ReceiptToGuestService{

    private final ReceiptToGuestRepository receiptToGuestRepository;

    @Override
    @Transactional
    public void saveGuests(Long receiptId, List<Long> guestIds) {

        if (guestIds == null || guestIds.isEmpty())
            return;

        List<ReceiptToGuest> receiptToGuests = guestIds.stream()
                .map(guestId -> ReceiptToGuest.builder()
                        .receiptId(receiptId)
                        .guestId(guestId)
                        .build())
                .toList();

        receiptToGuestRepository.saveAll(receiptToGuests);
    }
}
