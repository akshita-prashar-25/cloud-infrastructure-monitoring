package com.monitoring.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monitoring.system.entity.Alert;

public interface AlertRepository
        extends JpaRepository<Alert, Long> {

    // =========================
    // SERVER-BASED ALERTS
    // CPU / MEMORY / DISK
    // =========================

    boolean existsByServerIdAndTypeAndStatus(
            Long serverId,
            String type,
            String status
    );

    List<Alert> findByServerIdAndTypeAndStatus(
            Long serverId,
            String type,
            String status
    );

    // =========================
    // AGENT-SPECIFIC ALERTS
    // =========================

    boolean existsByServerIdAndAgentIdAndTypeAndStatus(
            Long serverId,
            Long agentId,
            String type,
            String status
    );

    List<Alert> findByServerIdAndAgentIdAndTypeAndStatus(
            Long serverId,
            Long agentId,
            String type,
            String status
    );
}