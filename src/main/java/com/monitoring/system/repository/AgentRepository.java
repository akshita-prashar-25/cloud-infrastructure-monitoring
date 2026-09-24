package com.monitoring.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monitoring.system.entity.Agent;

public interface AgentRepository
        extends JpaRepository<Agent, Long> {

    Optional<Agent> findByApiKey(String apiKey);

    List<Agent> findByServerId(Long serverId);

    boolean existsByServerId(Long serverId);
}