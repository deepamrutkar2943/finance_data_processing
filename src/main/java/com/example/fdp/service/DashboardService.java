package com.example.fdp.service;

import com.example.fdp.dto.response.DashboardSummaryResponse;
import com.example.fdp.dto.response.InsightResponse;

public interface DashboardService {
    DashboardSummaryResponse getSummary();
    InsightResponse getInsights();
}