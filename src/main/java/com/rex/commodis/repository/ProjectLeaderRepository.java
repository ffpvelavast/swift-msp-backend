/** Spring Data JPA repository for ProjectLeader entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.ProjectLeader;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectLeaderRepository
        extends JpaRepository<ProjectLeader, Long>, JpaSpecificationExecutor<ProjectLeader> {
    List<ProjectLeader> findByProjectNumberId(Long projectNumberId);

    List<ProjectLeader> findByRexUserId(Long rexUserId);
    void deleteByProjectNumberId(Long projectNumberId);

    Optional<ProjectLeader> findByProjectNumberIdAndRexUserId(Long projectNumberId, Long rexUserId);
    Boolean existsByProjectNumberId(Long projectNumberId);

}
