/** Service implementation for Picklist business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.dto.PicklistCreateDto;
import com.rex.commodis.dto.PicklistDto;
import com.rex.commodis.dto.PicklistMemberDto;
import com.rex.commodis.dto.PicklistCreateResponseDto;
import com.rex.commodis.dto.PicklistSyncMembersRequestDto;
import com.rex.commodis.dto.PicklistSyncMembersResponseDto;
import com.rex.commodis.exceptions.ConflictException;
import com.rex.commodis.exceptions.ForbiddenException;
import com.rex.commodis.model.Picklist;
import com.rex.commodis.model.PicklistToBeneficiary;
import com.rex.commodis.model.PicklistToGuest;
import com.rex.commodis.repository.PicklistRepository;
import com.rex.commodis.repository.PicklistToBeneficiaryRepository;
import com.rex.commodis.repository.PicklistToGuestRepository;
import com.rex.commodis.repository.events.TeamEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of PicklistService
 */
@Service
@RequiredArgsConstructor
public class PicklistServiceImpl implements PicklistService {

    private final PicklistRepository picklistRepository;
    private final PicklistToBeneficiaryRepository picklistToBeneficiaryRepository;
    private final PicklistToGuestRepository picklistToGuestRepository;
    private final JdbcTemplate jdbcTemplate;
    private final TeamEventRepository teamEventRepository;

    @Override
    public List<PicklistMemberDto> getPicklistMembers(Long picklistId, Long rexUserId) {
        List<Object[]> results = picklistRepository.findPicklistMembersRaw(picklistId, rexUserId);

        return results.stream()
                .map(row -> PicklistMemberDto.builder()
                        .picklistId(row[0] != null ? ((Number) row[0]).longValue() : null)
                        .memberType(row[1] != null ? row[1].toString() : null)
                        .memberId(row[2] != null ? ((Number) row[2]).longValue() : null)
                        .isFavorite(row[3] != null ? (Boolean) row[3] : false)
                        .listname(row[4] != null ? row[4].toString() : null)
                        .organization(row[5] != null ? row[5].toString() : null)
                        .identityTag(row[6] != null ? row[6].toString() : null)
                        .orgUnit(row[7] != null ? row[7].toString() : null)
                        .costCenter(row[8] != null ? row[8].toString() : null)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PicklistDto> getUserPicklists(Long rexUserId) {
        List<Picklist> picklists = picklistRepository.findAllByRexUserIdOrderBySortIdAsc(rexUserId);

        return picklists.stream()
                .map(p -> PicklistDto.builder()
                        .id(p.getId())
                        .name(p.getDisplayName())
                        .sortId(p.getSortId())
                        .createdAt(p.getCreatedAt())
                        .memberCount(picklistRepository.countMembers(p.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PicklistCreateResponseDto createPicklist(PicklistCreateDto request, Long rexUserId) {
        Instant now = Instant.now();
        Picklist picklist = Picklist.builder()
                .displayName(request.getName())
                .rexUserId(rexUserId)
                .createdAt(now)
                .sortId((short) 1)
                .build();

        picklist = picklistRepository.saveAndFlush(picklist);
        Long picklistId = picklist.getId();

        long memberCount = 0;

        if (request.getBeneficiaryIds() != null && !request.getBeneficiaryIds().isEmpty()) {
            List<Object[]> beneficiaryBatch = request.getBeneficiaryIds().stream()
                    .filter(benId -> benId != null && benId > 0)
                    .map(benId -> new Object[] { picklistId, benId })
                    .collect(Collectors.toList());

            if (!beneficiaryBatch.isEmpty()) {
                final String sqlBen = "INSERT INTO rexbase.picklist_to_beneficiary (picklist_id, beneficiary_id, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
                jdbcTemplate.batchUpdate(sqlBen, beneficiaryBatch);
                memberCount += beneficiaryBatch.size();
            }
        }

        if (request.getGuestIds() != null && !request.getGuestIds().isEmpty()) {
            List<Object[]> guestBatch = request.getGuestIds().stream()
                    .filter(guestId -> guestId != null && guestId > 0)
                    .map(guestId -> new Object[] { picklistId, guestId })
                    .collect(Collectors.toList());

            if (!guestBatch.isEmpty()) {
                final String sqlGuest = "INSERT INTO rexbase.picklist_to_guest (picklist_id, guest_id, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
                jdbcTemplate.batchUpdate(sqlGuest, guestBatch);
                memberCount += guestBatch.size();
            }
        }

        return PicklistCreateResponseDto.builder()
                .picklistId(picklistId)
                .build();
    }

    @Override
    @Transactional
    public void batchUpdateFavorites(List<Long> beneficiaryIds, Long rexUserId) {
        if (beneficiaryIds == null || beneficiaryIds.isEmpty())
            return;

        // 1. Find or create the personal "My Favorites" picklist
        Long favoriteListId = picklistRepository.findByRexUserIdAndDisplayName(rexUserId, "My Favorites")
                .map(Picklist::getId)
                .orElseGet(() -> {
                    Picklist p = Picklist.builder()
                            .displayName("My Favorites")
                            .rexUserId(rexUserId)
                            .createdAt(Instant.now())
                            .sortId((short) 0)
                            .build();
                    return picklistRepository.saveAndFlush(p).getId();
                });

        for (Long benId : beneficiaryIds) {
            // Check if it already exists in the favorites picklist
            String checkSql = "SELECT COUNT(*) FROM rexbase.picklist_to_beneficiary WHERE picklist_id = ? AND beneficiary_id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, favoriteListId, benId);

            if (count != null && count > 0) {
                // TOGGLE OFF: Remove from both systems
                String deletePicklist = "DELETE FROM rexbase.picklist_to_beneficiary WHERE picklist_id = ? AND beneficiary_id = ?";
                jdbcTemplate.update(deletePicklist, favoriteListId, benId);

                String deleteLegacy = "DELETE FROM rexbase.beneficiary_favorites WHERE rexuser_id = ? AND beneficiary_id = ?";
                jdbcTemplate.update(deleteLegacy, rexUserId, benId);
            } else {
                // TOGGLE ON: Add to both systems
                String insertPicklist = "INSERT INTO rexbase.picklist_to_beneficiary (picklist_id, beneficiary_id, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
                jdbcTemplate.update(insertPicklist, favoriteListId, benId);

                String insertLegacy = "INSERT INTO rexbase.beneficiary_favorites (id, rexuser_id, beneficiary_id, sortid, created_at) "
                        +
                        "VALUES (nextval('rexbase.beneficiary_favorites_id_seq'), ?, ?, 0, CURRENT_TIMESTAMP)";
                jdbcTemplate.update(insertLegacy, rexUserId, benId);
            }
        }
    }

    @Override
    @Transactional
    public PicklistSyncMembersResponseDto syncMembers(Long picklistId, PicklistSyncMembersRequestDto request, Long rexUserId) {
        Picklist picklist = picklistRepository.findByIdAndRexUserId(picklistId, rexUserId)
                .orElseThrow(() -> new ForbiddenException("User cannot edit picklist"));

        if (request.getName() != null && !request.getName().isBlank()) {
            picklistRepository.findByRexUserIdAndDisplayName(rexUserId, request.getName())
                    .filter(existing -> !existing.getId().equals(picklistId))
                    .ifPresent(existing -> {
                        throw new ConflictException("A picklist with that name already exists");
                    });

            picklist.setDisplayName(request.getName());
            picklist.setUpdatedAt(Instant.now());
        }

        List<Long> beneficiaryIds = request.getBeneficiaryIds() != null ? request.getBeneficiaryIds() : List.of();
        List<Long> guestIds = request.getGuestIds() != null ? request.getGuestIds() : List.of();

        replaceAllBeneficiaries(picklistId, beneficiaryIds);
        replaceAllGuests(picklistId, guestIds);

        return PicklistSyncMembersResponseDto.builder()
                .picklistId(picklistId)
                .picklistName(picklist.getDisplayName())
                .beneficiaryIds(beneficiaryIds)
                .guestIds(guestIds)
                .build();
    }

    @Override
    @Transactional
    public void deletePicklistById(Long picklistId, Long rexUserId) {
        Picklist picklist = picklistRepository.findByIdAndRexUserId(picklistId, rexUserId)
                .orElseThrow(() -> new ForbiddenException("User not allowed to delete this picklist"));

        Integer processedCount = teamEventRepository.countProcessedEvents(picklistId);
        if (processedCount != null && processedCount > 0) {
            throw new ConflictException("Picklist is used in processed team events");
        }

        teamEventRepository.detachSafeTeamEvents(picklistId);

        picklistToBeneficiaryRepository.deleteByPicklistId(picklistId);
        picklistToGuestRepository.deleteByPicklistId(picklistId);

        picklistRepository.delete(picklist);
    }

    private void replaceAllBeneficiaries(Long picklistId, List<Long> beneficiaryIds){
        //Delete existing picklist entries
        picklistToBeneficiaryRepository.deleteByPicklistId(picklistId);

        //Build new picklist entries
        List<PicklistToBeneficiary> picklistToBeneficiaries =
                beneficiaryIds.stream()
                        .map(beneficiaryId -> PicklistToBeneficiary.builder()
                                .picklistId(picklistId)
                                .beneficiaryId(beneficiaryId)
                                .build())
                        .toList();

        //Save to DB
        picklistToBeneficiaryRepository.saveAll(picklistToBeneficiaries);
    }

    private void replaceAllGuests(Long picklistId, List<Long> guestIds){
        //Delete existing picklist entries
        picklistToGuestRepository.deleteByPicklistId(picklistId);

        //Build new picklist entries
        List<PicklistToGuest> picklistToGuests =
                guestIds.stream()
                        .map(guestId -> PicklistToGuest.builder()
                                .picklistId(picklistId)
                                .guestId(guestId)
                                .build())
                        .toList();

        //Save to DB
        picklistToGuestRepository.saveAll(picklistToGuests);
    }

}