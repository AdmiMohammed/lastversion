package com.example.einternmatchback.admin.controller;

import com.example.einternmatchback.admin.dto.AdminDashboardStatsDTO;
import com.example.einternmatchback.admin.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;
    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardController.class);

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping
    public ResponseEntity<?> getDashboardStats() {
        try {
            AdminDashboardStatsDTO stats = adminDashboardService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error in admin dashboard", e);
            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "error", "Internal server error",
                            "message", e.getMessage()
                    )
            );
        }
    }
}