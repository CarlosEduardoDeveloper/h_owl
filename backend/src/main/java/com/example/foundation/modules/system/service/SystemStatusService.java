package com.example.foundation.modules.system.service;

import com.example.foundation.modules.system.dto.SystemStatusResponse;
import org.springframework.stereotype.Service;

@Service
public class SystemStatusService {

    public SystemStatusResponse getStatus() {
        return new SystemStatusResponse("UP");
    }
}