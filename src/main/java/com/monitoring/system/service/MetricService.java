package com.monitoring.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.monitoring.system.entity.Alert;
import com.monitoring.system.entity.Metric;
import com.monitoring.system.entity.Server;
import com.monitoring.system.repository.AlertRepository;
import com.monitoring.system.repository.MetricRepository;
import com.monitoring.system.repository.ServerRepository;
import com.monitoring.system.exception.ResourceNotFoundException;

@Service
public class MetricService {

    private final MetricRepository metricRepository;
    private final AlertRepository alertRepository;
    private final ServerRepository serverRepository;

    public MetricService(
            MetricRepository metricRepository,
            AlertRepository alertRepository,
            ServerRepository serverRepository) {

        this.metricRepository = metricRepository;
        this.alertRepository = alertRepository;
        this.serverRepository = serverRepository;
    }

    public Metric addMetric(Metric metric) {
        // =========================
        // MARK SERVER AS UP
        // =========================

        Server server = serverRepository
                .findById(metric.getServerId())
                .orElse(null);

        if (server != null) {
            server.setStatus("UP");
            serverRepository.save(server);
        }

        // =========================
        // SAVE METRIC
        // =========================

        Metric savedMetric = metricRepository.save(metric);

        // =========================
        // CPU MONITORING
        // =========================

        if (metric.getCpuUsage() > 80) {

            if (!alertRepository.existsByServerIdAndTypeAndStatus(
                    metric.getServerId(), "CPU", "ACTIVE")) {

                Alert alert = new Alert();

                alert.setServerId(metric.getServerId());
                alert.setType("CPU");
                alert.setSeverity(getSeverity(metric.getCpuUsage()));
                alert.setMessage("CPU usage exceeded 80%");
                alert.setStatus("ACTIVE");

                alertRepository.save(alert);
            }

        } else {

            resolveAlert(metric.getServerId(), "CPU");
        }

        // =========================
        // MEMORY MONITORING
        // =========================

        if (metric.getMemoryUsage() > 80) {

            if (!alertRepository.existsByServerIdAndTypeAndStatus(
                    metric.getServerId(), "MEMORY", "ACTIVE")) {

                Alert alert = new Alert();

                alert.setServerId(metric.getServerId());
                alert.setType("MEMORY");
                alert.setSeverity(getSeverity(metric.getMemoryUsage()));
                alert.setMessage("Memory usage exceeded 80%");
                alert.setStatus("ACTIVE");

                alertRepository.save(alert);
            }

        } else {

            resolveAlert(metric.getServerId(), "MEMORY");
        }

        // =========================
        // DISK MONITORING
        // =========================

        if (metric.getDiskUsage() > 80) {

            if (!alertRepository.existsByServerIdAndTypeAndStatus(
                    metric.getServerId(), "DISK", "ACTIVE")) {

                Alert alert = new Alert();

                alert.setServerId(metric.getServerId());
                alert.setType("DISK");
                alert.setSeverity(getSeverity(metric.getDiskUsage()));
                alert.setMessage("Disk usage exceeded 80%");
                alert.setStatus("ACTIVE");

                alertRepository.save(alert);
            }

        } else {

            resolveAlert(metric.getServerId(), "DISK");
        }

        return savedMetric;
    }

    // =========================
    // ALERT SEVERITY
    // =========================

    private String getSeverity(Double usage) {

        if (usage > 90) {
            return "CRITICAL";
        }

        return "HIGH";
    }

    // =========================
    // RESOLVE ALERT
    // =========================

    private void resolveAlert(Long serverId, String type) {

        List<Alert> activeAlerts =
                alertRepository.findByServerIdAndTypeAndStatus(
                        serverId,
                        type,
                        "ACTIVE");

        for (Alert alert : activeAlerts) {

            alert.setStatus("RESOLVED");

            alertRepository.save(alert);
        }
    }

    // =========================
    // GET ALL METRICS
    // =========================

    public List<Metric> getAllMetrics() {

        return metricRepository.findAll();
    }

    // =========================
    // GET METRIC BY ID
    // =========================

    public Metric getMetricById(Long id) {

        return metricRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Metric with ID "
                                + id
                                + " not found"
                        )
                );
    }

    // =========================
    // DELETE METRIC
    // =========================

    public void deleteMetric(Long id) {

        metricRepository.deleteById(id);
    }
}