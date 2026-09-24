package com.monitoring.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.monitoring.system.entity.Alert;
import com.monitoring.system.repository.AlertRepository;
import com.monitoring.system.exception.ResourceNotFoundException;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public Alert addAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Alert getAlertById(Long id) {

        return alertRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Alert with ID "
                                + id
                                + " not found"
                        )
                );
    }

    public void deleteAlert(Long id) {
        alertRepository.deleteById(id);
    }
}