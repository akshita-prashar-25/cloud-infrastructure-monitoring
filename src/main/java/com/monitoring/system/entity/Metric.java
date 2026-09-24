package com.monitoring.system.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Data
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Server ID is required")
    private Long serverId;

    @NotNull(message = "CPU usage is required")
    @Min(value = 0, message = "CPU usage cannot be negative")
    @Max(value = 100, message = "CPU usage cannot exceed 100")
    private Double cpuUsage;

    @NotNull(message = "Memory usage is required")
    @Min(value = 0, message = "Memory usage cannot be negative")
    @Max(value = 100, message = "Memory usage cannot exceed 100")
    private Double memoryUsage;

    @NotNull(message = "Disk usage is required")
    @Min(value = 0, message = "Disk usage cannot be negative")
    @Max(value = 100, message = "Disk usage cannot exceed 100")
    private Double diskUsage;

    @Min(value = 0, message = "Bytes sent cannot be negative")
    private Long bytesSent;

    @Min(value = 0, message = "Bytes received cannot be negative")
    private Long bytesReceived;

    @Min(value = 0, message = "Upload speed cannot be negative")
    private Double uploadSpeed;

    @Min(value = 0, message = "Download speed cannot be negative")
    private Double downloadSpeed;

    @Min(value = 0, message = "Uptime cannot be negative")
    private Long uptimeSeconds;

    private LocalDateTime timestamp;

    @PrePersist
    public void setTimestamp() {
        timestamp = LocalDateTime.now();
    }
}