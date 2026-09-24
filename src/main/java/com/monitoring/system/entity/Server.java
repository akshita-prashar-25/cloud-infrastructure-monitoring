package com.monitoring.system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Server name is required")
    private String name;

    @NotBlank(message = "IP address is required")
    private String ipAddress;

    private String status;
}