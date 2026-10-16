/** Service interface defining PicklistToGuest operations; implemented by PicklistToGuestServiceImpl. */
package com.rex.commodis.service;

import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.dto.PicklistToGuestDto;

import java.util.List;

public interface PicklistToGuestService {

    @Transactional
    PicklistToGuestDto save(PicklistToGuestDto dto);

    @Transactional(readOnly = true)
    PicklistToGuestDto get(Long picklistId, Long guestId);

    @Transactional
    void delete(Long picklistId, Long guestId);

    @Transactional(readOnly = true)
    List<PicklistToGuestDto> listByPicklist(Long picklistId);

    @Transactional(readOnly = true)
    List<PicklistToGuestDto> listByGuest(Long guestId);
}
