package com.monitoring.system.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.monitoring.system.entity.Agent;
import com.monitoring.system.entity.Alert;
import com.monitoring.system.entity.Metric;
import com.monitoring.system.entity.Server;
import com.monitoring.system.repository.AgentRepository;
import com.monitoring.system.repository.AlertRepository;
import com.monitoring.system.repository.MetricRepository;
import com.monitoring.system.repository.ServerRepository;

@Service
public class ServerStatusService {

    private final ServerRepository serverRepository;
    private final MetricRepository metricRepository;
    private final AgentRepository agentRepository;
    private final AlertRepository alertRepository;

    public ServerStatusService(
            ServerRepository serverRepository,
            MetricRepository metricRepository,
            AgentRepository agentRepository,
            AlertRepository alertRepository) {

        this.serverRepository = serverRepository;
        this.metricRepository = metricRepository;
        this.agentRepository = agentRepository;
        this.alertRepository = alertRepository;
    }

    // =========================
    // CHECK SERVER + AGENT STATUS
    // =========================

    @Scheduled(fixedRate = 15000)
    public void checkServerStatus() {

        List<Server> servers =
                serverRepository.findAll();

        LocalDateTime currentTime =
                LocalDateTime.now();

        for (Server server : servers) {

            checkServerHealth(
                    server,
                    currentTime
            );

            checkAgentHealth(
                    server,
                    currentTime
            );
        }
    }

    // =========================
    // SERVER HEALTH
    // =========================

    private void checkServerHealth(
            Server server,
            LocalDateTime currentTime) {

        Metric latestMetric =
                metricRepository
                        .findTopByServerIdOrderByTimestampDesc(
                                server.getId()
                        )
                        .orElse(null);

        if (latestMetric == null) {

            server.setStatus("DOWN");

        } else {

            long secondsSinceLastMetric =
                    Duration.between(
                            latestMetric.getTimestamp(),
                            currentTime
                    ).getSeconds();

            if (secondsSinceLastMetric <= 30) {

                server.setStatus("UP");

            } else {

                server.setStatus("DOWN");
            }
        }

        serverRepository.save(server);
    }

    // =========================
    // AGENT HEALTH
    // =========================

    private void checkAgentHealth(
            Server server,
            LocalDateTime currentTime) {

        List<Agent> agents =
                agentRepository
                        .findByServerId(
                                server.getId()
                        );

        if (agents.isEmpty()) {
            return;
        }

        for (Agent agent : agents) {

            checkIndividualAgentHealth(
                    server,
                    agent,
                    currentTime
            );
        }
    }

    // =========================
    // CHECK INDIVIDUAL AGENT
    // =========================

    private void checkIndividualAgentHealth(
            Server server,
            Agent agent,
            LocalDateTime currentTime) {

        // =========================
        // INACTIVE AGENT
        // =========================

        if (
                !"ACTIVE".equals(
                        agent.getStatus()
                )
        ) {

            resolveAgentOfflineAlert(
                    server.getId(),
                    agent.getId()
            );

            return;
        }

        // =========================
        // NO LAST SEEN
        // =========================

        if (agent.getLastSeen() == null) {

            createAgentOfflineAlert(
                    server,
                    agent
            );

            return;
        }

        long secondsSinceLastSeen =
                Duration.between(
                        agent.getLastSeen(),
                        currentTime
                ).getSeconds();

        // =========================
        // AGENT ONLINE
        // =========================

        if (secondsSinceLastSeen <= 30) {

            resolveAgentOfflineAlert(
                    server.getId(),
                    agent.getId()
            );

        }

        // =========================
        // AGENT OFFLINE
        // =========================

        else {

            createAgentOfflineAlert(
                    server,
                    agent
            );
        }
    }

    // =========================
    // CREATE AGENT OFFLINE ALERT
    // =========================

    private void createAgentOfflineAlert(
            Server server,
            Agent agent) {

        boolean alertExists =
                alertRepository
                        .existsByServerIdAndAgentIdAndTypeAndStatus(
                                server.getId(),
                                agent.getId(),
                                "AGENT",
                                "ACTIVE"
                        );

        // =========================
        // PREVENT DUPLICATE ALERTS
        // =========================

        if (alertExists) {
            return;
        }

        Alert alert =
                new Alert();

        alert.setServerId(
                server.getId()
        );

        alert.setAgentId(
                agent.getId()
        );

        alert.setType(
                "AGENT"
        );

        alert.setSeverity(
                "HIGH"
        );

        alert.setMessage(
                "Monitoring agent is offline"
        );

        alert.setStatus(
                "ACTIVE"
        );

        alertRepository.save(alert);
    }

    // =========================
    // RESOLVE AGENT OFFLINE ALERT
    // =========================

    private void resolveAgentOfflineAlert(
            Long serverId,
            Long agentId) {

        List<Alert> activeAlerts =
                alertRepository
                        .findByServerIdAndAgentIdAndTypeAndStatus(
                                serverId,
                                agentId,
                                "AGENT",
                                "ACTIVE"
                        );

        for (Alert alert : activeAlerts) {

            alert.setStatus(
                    "RESOLVED"
            );

            alertRepository.save(alert);
        }
    }
}