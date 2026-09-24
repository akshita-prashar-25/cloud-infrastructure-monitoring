package com.monitoring.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monitoring.system.entity.Server;

public interface ServerRepository
        extends JpaRepository<Server, Long> {

    Optional<Server> findByIpAddress(
            String ipAddress
    );

}