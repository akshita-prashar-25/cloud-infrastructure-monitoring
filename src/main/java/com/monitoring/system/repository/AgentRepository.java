package com.monitoring.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monitoring.system.entity.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    Optional<Agent> findByApiKey(String apiKey);

    List<Agent> findByServerId(Long serverId);

    Optional<Agent> findByServerIdAndHostnameAndIpAddress(
            Long serverId,
            String hostname,
            String ipAddress
    );
    
    Optional<Agent> findFirstByHostnameAndIpAddressOrderByIdAsc(
            String hostname,
            String ipAddress
    );
}