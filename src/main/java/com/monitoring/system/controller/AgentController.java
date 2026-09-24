package com.monitoring.system.controller;

import java.util.List;

import java.util.ArrayList;

import com.monitoring.system.dto.AgentResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monitoring.system.entity.Agent;
import com.monitoring.system.service.AgentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    private final AgentService agentService;


    public AgentController(
            AgentService agentService) {

        this.agentService =
                agentService;

    }


    // =========================
    // REGISTER AGENT
    // =========================

    @PostMapping("/register")
    public Agent registerAgent(
           @Valid  @RequestBody Agent agent) {

        return agentService
                .registerAgent(agent);

    }


    // =========================
    // GET ALL AGENTS
    // =========================

    @GetMapping
    public List<AgentResponse> getAllAgents() {

        List<Agent> agents =
                agentService.getAllAgents();

        List<AgentResponse> responses =
                new ArrayList<>();

        for (Agent agent : agents) {

            AgentResponse response =
                    new AgentResponse();

            response.setId(
                    agent.getId()
            );

            response.setServerId(
                    agent.getServerId()
            );

            response.setAgentName(
                    agent.getAgentName()
            );

            response.setHostname(
                    agent.getHostname()
            );

            response.setIpAddress(
                    agent.getIpAddress()
            );

            response.setStatus(
                    agent.getStatus()
            );

            response.setConnectionStatus(
                    agentService.getConnectionStatus(
                            agent
                    )
            );

            response.setRegisteredAt(
                    agent.getRegisteredAt()
            );

            response.setLastSeen(
                    agent.getLastSeen()
            );

            responses.add(
                    response
            );
        }

        return responses;
    } 

    // =========================
    // GET AGENT BY ID
    // =========================

    @GetMapping("/{id}")
    public Agent getAgentById(
            @PathVariable Long id) {

        return agentService
                .getAgentById(id);

    }


    // =========================
    // UPDATE AGENT STATUS
    // =========================

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateAgentStatus(

            @PathVariable Long id,

            @RequestBody String status) {


        // Remove quotation marks if
        // Postman sends "INACTIVE"

        status =
                status.replace(
                        "\"",
                        ""
                );


        if (
                !"ACTIVE".equals(status) &&
                !"INACTIVE".equals(status)
        ) {

            return ResponseEntity
                    .status(
                            HttpStatus.BAD_REQUEST
                    )
                    .body(
                            "Status must be ACTIVE or INACTIVE"
                    );

        }


        Agent updatedAgent =
                agentService
                        .updateAgentStatus(
                                id,
                                status
                        );


        if (updatedAgent == null) {

            return ResponseEntity
                    .status(
                            HttpStatus.NOT_FOUND
                    )
                    .body(
                            "Agent not found"
                    );

        }


        return ResponseEntity.ok(
                updatedAgent
        );

    }


    // =========================
    // DELETE AGENT
    // =========================

    @DeleteMapping("/{id}")
    public void deleteAgent(
            @PathVariable Long id) {

        agentService
                .deleteAgent(id);

    }

}