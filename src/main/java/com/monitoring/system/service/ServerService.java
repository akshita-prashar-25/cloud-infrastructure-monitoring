package com.monitoring.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.monitoring.system.entity.Server;
import com.monitoring.system.repository.ServerRepository;
import com.monitoring.system.exception.ResourceNotFoundException;

@Service
public class ServerService {

    private final ServerRepository serverRepository;

    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public Server addServer(Server server) {
        return serverRepository.save(server);
    }

    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

    public Server getServerById(Long id) {

        return serverRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Server with ID "
                                + id
                                + " not found"
                        )
                );
    }

    public void deleteServer(Long id) {
        serverRepository.deleteById(id);
    }
}