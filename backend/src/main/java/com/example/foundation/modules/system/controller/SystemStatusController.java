package com.example.foundation.modules.system.controller;

import com.example.foundation.modules.system.dto.SystemStatusResponse;
import com.example.foundation.modules.system.service.SystemStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system")
public class SystemStatusController {

    private final SystemStatusService systemStatusService;

    public SystemStatusController(SystemStatusService systemStatusService) {
        this.systemStatusService = systemStatusService;
    }

    @GetMapping("/status")
    public ResponseEntity<SystemStatusResponse> getStatus() {
        return ResponseEntity.ok(systemStatusService.getStatus());
    }
}