/** Service interface defining Guest operations; implemented by GuestServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.GuestCreateDto;
import com.rex.commodis.dto.GuestResponseDto;

import com.rex.commodis.dto.GuestCreateResponseDto;

import java.util.List;

public interface GuestService {
    GuestCreateResponseDto createGuest(GuestCreateDto request, Long rexUserId, Long platformId);

    List<GuestResponseDto> getGuests(Long rexUserId, Long platformId, String locale);
    
    List<GuestResponseDto> getGuestsPaged(Long rexUserId, Long platformId, String locale, int limit, int offset);
}
