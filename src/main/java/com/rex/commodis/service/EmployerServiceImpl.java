/** Service implementation for Employer business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.dto.EmployerDto;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.mapper.EmployerMapper;
import com.rex.commodis.model.Employer;
import com.rex.commodis.repository.EmployerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final EmployerMapper employerMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EmployerDto> fetchAll() {
        return employerRepository.findAll().stream()
                .map(employerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployerDto> fetchByPlatformId(Long platformId) {
        return employerRepository.findAll().stream()
                .filter(e -> e.getPlatformId().equals(platformId))
                .map(employerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployerDto getById(Long id) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employer not found with id: " + id));
        return employerMapper.toDto(employer);
    }

    @Override
    @Transactional
    public EmployerDto create(EmployerDto dto) {
        Employer entity = employerMapper.toEntity(dto);
        entity.setId(null);
        Employer saved = employerRepository.save(entity);
        return employerMapper.toDto(saved);
    }

    @Override
    @Transactional
    public EmployerDto update(Long id, EmployerDto dto) {
        Employer existing = employerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employer not found with id: " + id));
        existing.setEmployerUuid(dto.getEmployerUuid());
        existing.setDisplayName(dto.getDisplayName());
        existing.setActiveFrom(dto.getActiveFrom());
        existing.setActiveThrough(dto.getActiveThrough());
        existing.setUpdatedAt(dto.getUpdatedAt());
        Employer updated = employerRepository.save(existing);
        return employerMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!employerRepository.existsById(id)) {
            throw new NotFoundException("Employer not found with id: " + id);
        }
        employerRepository.deleteById(id);
    }
}
