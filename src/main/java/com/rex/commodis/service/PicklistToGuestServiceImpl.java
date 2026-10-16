/** Service implementation for PicklistToGuest business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.dto.PicklistToGuestDto;
import com.rex.commodis.mapper.PicklistToGuestMapper;
import com.rex.commodis.model.PicklistToGuest;
import com.rex.commodis.model.PicklistToGuestId;
import com.rex.commodis.repository.PicklistToGuestRepository;

import java.util.List;

@Service
public class PicklistToGuestServiceImpl implements PicklistToGuestService {

    private final PicklistToGuestRepository repository;
    private final PicklistToGuestMapper mapper;

    public PicklistToGuestServiceImpl(PicklistToGuestRepository repository,
                                      PicklistToGuestMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public PicklistToGuestDto save(PicklistToGuestDto dto) {
        // MapStruct creates entity; let DB set created_at (DEFAULT LOCALTIMESTAMP)
        PicklistToGuest entity = mapper.toEntity(dto);
        entity.setCreatedAt(null); // IMPORTANT: do not try to set any synthetic id here

        PicklistToGuest saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PicklistToGuestDto get(Long picklistId, Long guestId) {
        PicklistToGuestId id = new PicklistToGuestId(picklistId, guestId);
        PicklistToGuest entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "PicklistToGuest link not found: picklistId=" + picklistId + ", guestId=" + guestId));
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(Long picklistId, Long guestId) {
        PicklistToGuestId id = new PicklistToGuestId(picklistId, guestId);
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PicklistToGuestDto> listByPicklist(Long picklistId) {
        return mapper.toDtoList(repository.findByPicklistId(picklistId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PicklistToGuestDto> listByGuest(Long guestId) {
        return mapper.toDtoList(repository.findByGuestId(guestId));
    }
}
