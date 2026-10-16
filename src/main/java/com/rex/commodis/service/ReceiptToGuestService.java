/** Service interface defining ReceiptToGuest operations; implemented by ReceiptToGuestServiceImpl. */
package com.rex.commodis.service;

import java.util.List;

public interface ReceiptToGuestService {

    void saveGuests(Long receiptId, List<Long> guestIds);
}
