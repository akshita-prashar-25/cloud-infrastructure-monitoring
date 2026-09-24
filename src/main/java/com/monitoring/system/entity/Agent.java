package com.monitoring.system.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long serverId;

    @NotBlank(message = "Agent name is required")
    private String agentName;

    @NotBlank(message = "Hostname is required")
    private String hostname;

    @NotBlank(message = "IP address is required")
    private String ipAddress;
    
    private String apiKey;

    private String status;

    private LocalDateTime registeredAt;

    private LocalDateTime lastSeen;


    @PrePersist
    public void setRegisteredAt() {

        registeredAt =
                LocalDateTime.now();

        lastSeen =
                LocalDateTime.now();
    }
}