package com.monitoring.system.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AgentResponse {

    private Long id;

    private Long serverId;

    private String agentName;

    private String hostname;

    private String ipAddress;

    private String status;

    private String connectionStatus;

    private LocalDateTime registeredAt;

    private LocalDateTime lastSeen;
}