package com.monitoring.system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.monitoring.system.entity.Server;
import com.monitoring.system.service.ServerService;

@RestController
@RequestMapping("/api/servers")
public class ServerController {

    private final ServerService serverService;

    public ServerController(
            ServerService serverService) {

        this.serverService =
                serverService;
    }

    @PostMapping
    public Server addServer(
            @Valid @RequestBody Server server) {

        return serverService
                .addServer(server);
    }

    @GetMapping
    public List<Server> getAllServers() {

        return serverService
                .getAllServers();
    }

    @GetMapping("/{id}")
    public Server getServerById(
            @PathVariable Long id) {

        return serverService
                .getServerById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteServer(
            @PathVariable Long id) {

        serverService
                .deleteServer(id);
    }
}