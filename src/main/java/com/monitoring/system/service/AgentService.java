package com.monitoring.system.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.monitoring.system.entity.Agent;
import com.monitoring.system.entity.Server;
import com.monitoring.system.exception.ResourceNotFoundException;
import com.monitoring.system.repository.AgentRepository;
import com.monitoring.system.repository.ServerRepository;

@Service
public class AgentService {

    private final AgentRepository agentRepository;
    private final ServerRepository serverRepository;

    public AgentService(
            AgentRepository agentRepository,
            ServerRepository serverRepository) {

        this.agentRepository = agentRepository;
        this.serverRepository = serverRepository;
    }

    // =========================
    // REGISTER AGENT
    // =========================

    public Agent registerAgent(Agent agent) {

        // =========================
        // CHECK EXISTING AGENT
        // =========================

        Agent existingAgent =
                agentRepository
                        .findFirstByHostnameAndIpAddressOrderByIdAsc(
                                agent.getHostname(),
                                agent.getIpAddress()
                        )
                        .orElse(null);

        // =========================
        // REUSE EXISTING AGENT
        // =========================

        if (existingAgent != null) {

            existingAgent.setStatus("ACTIVE");

            existingAgent.setLastSeen(
                    LocalDateTime.now()
            );

            return agentRepository.save(
                    existingAgent
            );
        }

        // =========================
        // FIND SERVER
        // =========================

        Server server = null;

        // First try server ID if one was provided
        if (agent.getServerId() != null) {

            server =
                    serverRepository
                            .findById(agent.getServerId())
                            .orElse(null);
        }

        // =========================
        // FIND SERVER BY IP
        // =========================

        if (server == null) {

            server =
                    serverRepository
                            .findByIpAddress(
                                    agent.getIpAddress()
                            )
                            .orElse(null);
        }

        // =========================
        // CREATE NEW SERVER
        // =========================

        if (server == null) {

            server = new Server();

            server.setName(
                    agent.getHostname()
            );

            server.setIpAddress(
                    agent.getIpAddress()
            );

            server.setStatus(
                    "DOWN"
            );

            server =
                    serverRepository.save(
                            server
                    );
        }

        // =========================
        // CREATE NEW AGENT
        // =========================

        agent.setServerId(
                server.getId()
        );

        agent.setApiKey(
                UUID.randomUUID().toString()
        );

        agent.setStatus(
                "ACTIVE"
        );

        agent.setLastSeen(
                LocalDateTime.now()
        );

        return agentRepository.save(
                agent
        );
    }
    // =========================
    // GET ALL AGENTS
    // =========================

    public List<Agent> getAllAgents() {

        return agentRepository.findAll();
    }

    // =========================
    // GET AGENT BY ID
    // =========================

    public Agent getAgentById(Long id) {

        return agentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Agent with ID "
                                        + id
                                        + " not found"
                        )
                );
    }

    // =========================
    // GET AGENT BY API KEY
    // =========================

    public Agent getAgentByApiKey(
            String apiKey) {

        return agentRepository
                .findByApiKey(apiKey)
                .orElse(null);
    }

    // =========================
    // VALIDATE API KEY
    // =========================

    public boolean isValidApiKey(
            String apiKey) {

        if (
                apiKey == null ||
                apiKey.isBlank()
        ) {

            return false;
        }

        Agent agent =
                agentRepository
                        .findByApiKey(apiKey)
                        .orElse(null);

        if (agent == null) {

            return false;
        }

        return "ACTIVE".equals(
                agent.getStatus()
        );
    }

    // =========================
    // UPDATE AGENT STATUS
    // =========================

    public Agent updateAgentStatus(
            Long id,
            String status) {

        Agent agent =
                agentRepository
                        .findById(id)
                        .orElse(null);

        if (agent == null) {

            return null;
        }

        agent.setStatus(
                status
        );

        return agentRepository.save(
                agent
        );
    }

    // =========================
    // UPDATE LAST SEEN
    // =========================

    public Agent updateLastSeen(
            Long agentId) {

        Agent agent =
                agentRepository
                        .findById(agentId)
                        .orElse(null);

        if (agent == null) {

            return null;
        }

        agent.setLastSeen(
                LocalDateTime.now()
        );

        return agentRepository.save(
                agent
        );
    }

    // =========================
    // CHECK AGENT CONNECTION
    // =========================

    public String getConnectionStatus(
            Agent agent) {

        if (agent == null) {
            return "OFFLINE";
        }

        if (agent.getLastSeen() == null) {
            return "OFFLINE";
        }

        long secondsSinceLastSeen =
                Duration.between(
                        agent.getLastSeen(),
                        LocalDateTime.now()
                ).getSeconds();

        if (secondsSinceLastSeen <= 30) {
            return "ONLINE";
        }

        return "OFFLINE";
    }

    // =========================
    // DELETE AGENT
    // =========================

    public void deleteAgent(
            Long id) {

        agentRepository.deleteById(
                id
        );
    }
}