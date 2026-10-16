/** Service implementation for Guest business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.dto.GuestCreateDto;
import com.rex.commodis.dto.GuestCreateResponseDto;
import com.rex.commodis.dto.GuestResponseDto;
import com.rex.commodis.model.Guest;
import com.rex.commodis.model.GuestType;
import com.rex.commodis.repository.GuestRepository;
import com.rex.commodis.repository.GuestTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final GuestTypeRepository guestTypeRepository;

    @Override
    @Transactional
    public GuestCreateResponseDto createGuest(GuestCreateDto request, Long rexUserId, Long platformId) {
        String fullName = request.getName().trim();
        String firstname;
        String lastname;

        // Apply "First Space" Splitting Logic
        int firstSpace = fullName.indexOf(" ");
        if (firstSpace != -1) {
            firstname = fullName.substring(0, firstSpace).trim();
            lastname = fullName.substring(firstSpace + 1).trim();
        } else {
            firstname = fullName;
            lastname = ""; // Professional: leave empty instead of "*unbekannt*"
        }

        // Find the GuestType using guest_enum - Strict Validation for Production
        GuestType guestType = guestTypeRepository.findFirstByGuestEnum(request.getType().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Invalid guest type: " + request.getType()));

        // Check for duplicates across the entire organisation
        java.util.Optional<Guest> existingGuest = guestRepository
                .findFirstByPlatformIdAndFirstnameIgnoreCaseAndLastnameIgnoreCaseAndOrganizationIgnoreCase(
                        platformId, firstname, lastname, request.getOrganization());

        if (existingGuest.isPresent()) {
            Guest guest = existingGuest.get();
            return GuestCreateResponseDto.builder()
                    .guestId(guest.getId())
                    .build();
        }

        String jobTitle = request.getJobTitle() != null ? request.getJobTitle().trim() : "";
        Boolean taxationAbroad = request.getTaxationAbroad() != null ? request.getTaxationAbroad() : Boolean.FALSE;

        Guest guest = Guest.builder()
                .firstname(firstname)
                .lastname(lastname)
                .organization(request.getOrganization())
                .jobTitle(jobTitle)
                .defaultGuestType(guestType)
                .platformId(platformId)
                .creatorRexUserId(rexUserId)
                .organisationwideGuest(true) // Entire organisation can view
                .taxationAbroad(taxationAbroad)
                .build();

        guest = guestRepository.save(guest);

        return GuestCreateResponseDto.builder()
                .guestId(guest.getId())
                .build();

    }

    @Override
    public List<GuestResponseDto> getGuests(Long rexUserId, Long platformId, String locale) {
        List<GuestRepository.GuestProjection> rows = guestRepository.findGuestsRaw(platformId, rexUserId, locale);

        return rows.stream().map(row -> GuestResponseDto.builder()
                .id(row.getGuest_id())
                .organisationWide(row.getOrganisationwide_guest())
                .listname(row.getListname() != null ? row.getListname().toString() : null)
                .organization(row.getOrganization() != null ? row.getOrganization().toString() : null)
                .guestEnum(row.getGuest_enum() != null ? row.getGuest_enum().toString() : null)
                .guestType(row.getGuest_type() != null ? row.getGuest_type().toString() : null)
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<GuestResponseDto> getGuestsPaged(Long rexUserId, Long platformId, String locale, int limit, int offset) {
        List<GuestRepository.GuestProjection> rows = guestRepository.findGuestsPaged(platformId, rexUserId, locale, limit, offset);

        return rows.stream().map(row -> GuestResponseDto.builder()
                .id(row.getGuest_id())
                .organisationWide(row.getOrganisationwide_guest())
                .listname(row.getListname() != null ? row.getListname().toString() : null)
                .organization(row.getOrganization() != null ? row.getOrganization().toString() : null)
                .guestEnum(row.getGuest_enum() != null ? row.getGuest_enum().toString() : null)
                .guestType(row.getGuest_type() != null ? row.getGuest_type().toString() : null)
                .build()).collect(Collectors.toList());
    }
}