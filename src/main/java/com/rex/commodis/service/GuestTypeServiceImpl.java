/** Service implementation for GuestType business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.dto.GuestTypeDto;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.mapper.GuestTypeMapper;
import com.rex.commodis.model.GuestType;
import com.rex.commodis.repository.GuestTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestTypeServiceImpl implements GuestTypeService {

    private final GuestTypeRepository guestTypeRepository;
    private final GuestTypeMapper guestTypeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GuestTypeDto> fetchAll(String locale) {
        String loc = (locale == null) ? "de" : locale.toLowerCase();
        ObjectMapper mapper = new ObjectMapper();

        return guestTypeRepository.findAll().stream()
                .map(entity -> {
                    GuestTypeDto dto = guestTypeMapper.toDto(entity);
                    try {
                        com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(entity.getDisplayText());
                        dto.setDisplayText(node.has(loc) ? node.get(loc).asText()
                                : (node.has("en") ? node.get("en").asText() : entity.getDisplayText()));
                    } catch (Exception e) {
                        // Keep raw if parsing fails
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GuestTypeDto getById(Long id) {
        GuestType entity = guestTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("GuestType not found with id: " + id));
        return guestTypeMapper.toDto(entity);
    }

    @Override
    @Transactional
    public GuestTypeDto create(GuestTypeDto dto) {
        GuestType entity = guestTypeMapper.toEntity(dto);
        entity.setId(null);
        GuestType saved = guestTypeRepository.save(entity);
        return guestTypeMapper.toDto(saved);
    }

    @Override
    @Transactional
    public GuestTypeDto update(Long id, GuestTypeDto dto) {
        GuestType existing = guestTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("GuestType not found with id: " + id));
        existing.setGuestEnum(dto.getGuestEnum());
        existing.setDisplayText(dto.getDisplayText());
        existing.setBusinessGuest(dto.getBusinessGuest());
        GuestType updated = guestTypeRepository.save(existing);
        return guestTypeMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!guestTypeRepository.existsById(id)) {
            throw new NotFoundException("GuestType not found with id: " + id);
        }
        guestTypeRepository.deleteById(id);
    }
}
