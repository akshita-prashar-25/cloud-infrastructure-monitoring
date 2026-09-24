package com.monitoring.system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monitoring.system.entity.Agent;
import com.monitoring.system.entity.Metric;
import com.monitoring.system.service.AgentService;
import com.monitoring.system.service.MetricService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/metrics")
public class MetricController {

    private final MetricService metricService;

    private final AgentService agentService;
    
    @Value("${monitoring.api-key-header}")
    private String apiKeyHeader;


    public MetricController(
            MetricService metricService,
            AgentService agentService) {

        this.metricService =
                metricService;

        this.agentService =
                agentService;
    }


    // =========================
    // ADD METRIC
    // =========================

    @PostMapping
    public ResponseEntity<?> addMetric(
    		
    		@RequestHeader(
    		        value = "X-API-Key",
    		        required = false
    		)
    		String apiKey,

            @Valid @RequestBody Metric metric) {


        // =========================
        // VALIDATE API KEY
        // =========================

        if (!agentService.isValidApiKey(
                apiKey)) {

            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED
                    )
                    .body(
                            "Invalid or inactive API key"
                    );
        }


        // =========================
        // FIND AGENT
        // =========================

        Agent agent =
                agentService
                        .getAgentByApiKey(
                                apiKey
                        );


        if (agent == null) {

            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED
                    )
                    .body(
                            "Agent not found"
                    );
        }


        // =========================
        // VALIDATE SERVER ID
        // =========================

        if (
                agent.getServerId() == null
                ||
                metric.getServerId() == null
                ||
                !agent.getServerId()
                        .equals(
                                metric.getServerId())
        ) {

            return ResponseEntity
                    .status(
                            HttpStatus.FORBIDDEN)
                    .body(
                            "Agent is not authorized to send metrics for this server"
                    );
        }
        
     // =========================
     // UPDATE AGENT LAST SEEN
     // =========================

     agentService.updateLastSeen(
             agent.getId()
     );


        // =========================
        // SAVE METRIC
        // =========================

        Metric savedMetric =
                metricService.addMetric(
                        metric
                );


        return ResponseEntity.ok(
                savedMetric
        );

    }


    // =========================
    // GET ALL METRICS
    // =========================

    @GetMapping
    public List<Metric> getAllMetrics() {

        return metricService
                .getAllMetrics();

    }


    // =========================
    // GET METRIC BY ID
    // =========================

    @GetMapping("/{id}")
    public Metric getMetricById(
            @PathVariable Long id) {

        return metricService
                .getMetricById(id);

    }


    // =========================
    // DELETE METRIC
    // =========================

    @DeleteMapping("/{id}")
    public void deleteMetric(
            @PathVariable Long id) {

        metricService
                .deleteMetric(id);

    }

}