/** Service implementation for DeputyAssignment business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.dto.DeputyAssignmentDto;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.mapper.DeputyAssignmentMapper;
import com.rex.commodis.model.DeputyAssignment;
import com.rex.commodis.repository.DeputyAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DeputyAssignmentService.
 */
@Service
@RequiredArgsConstructor
public class DeputyAssignmentServiceImpl implements DeputyAssignmentService {

    private final DeputyAssignmentRepository deputyAssignmentRepository;
    private final DeputyAssignmentMapper deputyAssignmentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DeputyAssignmentDto> fetchByRexuserId(Long rexuserId) {
        return deputyAssignmentRepository.findAll().stream()
                .filter(a -> a.getRexuserId().equals(rexuserId))
                .map(deputyAssignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DeputyAssignmentDto create(DeputyAssignmentDto dto) {
        DeputyAssignment entity = deputyAssignmentMapper.toEntity(dto);
        entity.setId(null);
        DeputyAssignment saved = deputyAssignmentRepository.save(entity);
        return deputyAssignmentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public DeputyAssignmentDto update(Long id, DeputyAssignmentDto dto) {
        DeputyAssignment existing = deputyAssignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("DeputyAssignment not found with id: " + id));
        existing.setActiveFrom(dto.getActiveFrom());
        existing.setActiveThrough(dto.getActiveThrough());
        existing.setRexuserId(dto.getRexUserId());
        existing.setDeputyId(dto.getDeputyId());
        existing.setUpdatedAt(dto.getUpdatedAt());
        DeputyAssignment updated = deputyAssignmentRepository.save(existing);
        return deputyAssignmentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!deputyAssignmentRepository.existsById(id)) {
            throw new NotFoundException("DeputyAssignment not found with id: " + id);
        }
        deputyAssignmentRepository.deleteById(id);
    }
}
