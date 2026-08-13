package com.vehiclemaint.controller;

import com.vehiclemaint.dto.DashboardAlertDTO;
import com.vehiclemaint.dto.DashboardCostDTO;
import com.vehiclemaint.dto.DashboardSummaryDTO;
import com.vehiclemaint.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public DashboardSummaryDTO getSummary() {
        return dashboardService.getSummary();
    }

    @GetMapping("/alerts")
    public List<DashboardAlertDTO> getAlerts() {
        return dashboardService.getAlerts();
    }

    @GetMapping("/costs")
    public List<DashboardCostDTO> getCosts() {
        return dashboardService.getCosts();
    }
}
