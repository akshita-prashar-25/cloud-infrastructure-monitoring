package com.monitoring.system.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Entity
@Data
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long serverId;

    private Long agentId;

    private String type;

    private String severity;

    private String message;

    private LocalDateTime timestamp;

    private String status;

    @PrePersist
    public void setTimestamp() {
        timestamp = LocalDateTime.now();
    }
}