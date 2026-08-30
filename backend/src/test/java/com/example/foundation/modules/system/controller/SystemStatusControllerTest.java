package com.example.foundation.modules.system.controller;

import com.example.foundation.modules.system.dto.SystemStatusResponse;
import com.example.foundation.modules.system.service.SystemStatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SystemStatusController.class)
class SystemStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SystemStatusService systemStatusService;

    @Test
    void returnsSystemStatus() throws Exception {
        when(systemStatusService.getStatus()).thenReturn(new SystemStatusResponse("UP"));

        mockMvc.perform(get("/api/v1/system/status"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"status":"UP"}
                        """));
    }
}