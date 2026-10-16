/** Service implementation for DeputyRight business logic; called by the corresponding controller and delegates to the repository layer. */
//package com.rex.commodis.service;
//
//import com.rex.boilerplate.springboot.dto.DeputyRightDto;
//import com.rex.boilerplate.springboot.exceptions.NotFoundException;
//import com.rex.boilerplate.springboot.mapper.DeputyRightMapper;
//import com.rex.boilerplate.springboot.model.DeputyRight;
//import com.rex.boilerplate.springboot.repository.DeputyRightRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class DeputyRightServiceImpl implements DeputyRightService {
//
//    private final DeputyRightRepository deputyRightRepository;
//    private final DeputyRightMapper deputyRightMapper;
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<DeputyRightDto> fetchAll() {
//        return deputyRightRepository.findAll().stream()
//                .map(deputyRightMapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public DeputyRightDto getById(Long id) {
//        DeputyRight entity = deputyRightRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("DeputyRight not found with id: " + id));
//        return deputyRightMapper.toDto(entity);
//    }
//
//    @Override
//    @Transactional
//    public DeputyRightDto create(DeputyRightDto dto) {
//        DeputyRight entity = deputyRightMapper.toEntity(dto);
//        entity.setId(null);
//        DeputyRight saved = deputyRightRepository.save(entity);
//        return deputyRightMapper.toDto(saved);
//    }
//
//    @Override
//    @Transactional
//    public DeputyRightDto update(Long id, DeputyRightDto dto) {
//        DeputyRight existing = deputyRightRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("DeputyRight not found with id: " + id));
//        existing.setDeputyRightDe(dto.getDeputyRightDe());
//        existing.setDeputyRightEn(dto.getDeputyRightEn());
//        DeputyRight updated = deputyRightRepository.save(existing);
//        return deputyRightMapper.toDto(updated);
//    }
//
//    @Override
//    @Transactional
//    public void delete(Long id) {
//        if (!deputyRightRepository.existsById(id)) {
//            throw new NotFoundException("DeputyRight not found with id: " + id);
//        }
//        deputyRightRepository.deleteById(id);
//    }
//}



