/** Spring Data JPA repository for TeamEventStatus entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.enums.TeamEventStatusEnum;
import com.rex.commodis.model.events.TeamEventStatus;

import java.util.Optional;

@Repository
public interface TeamEventStatusRepository extends JpaRepository<TeamEventStatus, Long> {


    Optional<TeamEventStatus> findByStatusCode(TeamEventStatusEnum statusCode);
}