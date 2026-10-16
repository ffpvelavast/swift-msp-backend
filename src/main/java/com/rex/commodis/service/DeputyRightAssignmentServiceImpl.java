/** Service implementation for DeputyRightAssignment business logic; called by the corresponding controller and delegates to the repository layer. */
//package com.rex.commodis.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.rex.boilerplate.springboot.dto.DeputyRightAssignmentDto;
//import com.rex.boilerplate.springboot.mapper.DeputyRightAssignmentMapper;
//import com.rex.boilerplate.springboot.model.DeputyRightAssignment;
//import com.rex.boilerplate.springboot.model.DeputyRightAssignmentId;
//import com.rex.boilerplate.springboot.repository.DeputyRightAssignmentRepository;
//
//@Service
//public class DeputyRightAssignmentServiceImpl {
//
//    private final DeputyRightAssignmentRepository repository;
//    private final DeputyRightAssignmentMapper mapper;
//
//    public DeputyRightAssignmentServiceImpl(DeputyRightAssignmentRepository repository,
//                                            DeputyRightAssignmentMapper mapper) {
//        this.repository = repository;
//        this.mapper = mapper;
//    }
//
//    @Transactional
//    public DeputyRightAssignmentDto save(DeputyRightAssignmentDto dto) {
//        // Join table: just the two IDs; no timestamps/extra fields.
//        DeputyRightAssignment entity = mapper.toEntity(dto);
//        DeputyRightAssignment saved = repository.save(entity);
//        return mapper.toDto(saved);
//    }
//
//    @Transactional(readOnly = true)
//    public DeputyRightAssignmentDto get(Long deputyAssignmentId, Long deputyRightId) {
//        DeputyRightAssignmentId id = new DeputyRightAssignmentId(deputyAssignmentId, deputyRightId);
//        DeputyRightAssignment entity = repository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException(
//                        "DeputyRightAssignment not found for deputyAssignmentId=" + deputyAssignmentId +
//                        ", deputyRightId=" + deputyRightId));
//        return mapper.toDto(entity);
//    }
//
//    @Transactional
//    public void delete(Long deputyAssignmentId, Long deputyRightId) {
//        DeputyRightAssignmentId id = new DeputyRightAssignmentId(deputyAssignmentId, deputyRightId);
//        repository.deleteById(id);
//    }
//}



