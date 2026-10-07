package com.monitoring.system.dto;

import lombok.Data;

@Data
public class AgentRegistrationResponse {

    private Long id;

    private Long serverId;

    private String apiKey;

    private String status;
}