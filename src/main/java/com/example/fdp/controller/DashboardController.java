package com.example.fdp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fdp.dto.response.DashboardSummaryResponse;
import com.example.fdp.dto.response.InsightResponse;
import com.example.fdp.service.DashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard summary and insights")
public class DashboardController {

	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		super();
		this.dashboardService = dashboardService;
	}

	// ALL roles - summary
	@GetMapping("/summary")
	@PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
	@Operation(summary = "Get summary", description = "All roles - total income, expenses, net balance")
	public ResponseEntity<DashboardSummaryResponse> getSummary() {
		return ResponseEntity.ok(dashboardService.getSummary());
	}

	// ANALYST and ADMIN only - insights
	@GetMapping("/insights")
	@PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
	@Operation(summary = "Get insights", description = "Analyst and Admin - category wise totals and monthly trends")
	public ResponseEntity<InsightResponse> getInsights() {
		return ResponseEntity.ok(dashboardService.getInsights());
	}
}