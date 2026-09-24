package com.monitoring.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monitoring.system.entity.Metric;

public interface MetricRepository
        extends JpaRepository<Metric, Long> {

    Optional<Metric> findTopByServerIdOrderByTimestampDesc(
            Long serverId
    );
}