package com.rex.commodis.service;

import com.rex.commodis.repository.ProjectLeaderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectLeaderServiceImpl implements ProjectLeaderService{
    ProjectLeaderRepository projectLeaderRepository;
    public Boolean existsByProjectNumber(Long projectNumberId){
        return projectLeaderRepository.existsByProjectNumberId(projectNumberId);
    }
}
