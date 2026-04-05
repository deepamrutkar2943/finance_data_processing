package com.example.fdp.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public class InsightResponse {

	// e.g { "Salary" -> 50000, "Rent" -> 20000 }
	private Map<String, BigDecimal> categoryWiseTotals;

	// e.g { "2024-01" -> 30000, "2024-02" -> 45000 }
	private Map<String, BigDecimal> monthlyTrends;

	public Map<String, BigDecimal> getCategoryWiseTotals() {
		return categoryWiseTotals;
	}

	public void setCategoryWiseTotals(Map<String, BigDecimal> categoryWiseTotals) {
		this.categoryWiseTotals = categoryWiseTotals;
	}

	public Map<String, BigDecimal> getMonthlyTrends() {
		return monthlyTrends;
	}

	public void setMonthlyTrends(Map<String, BigDecimal> monthlyTrends) {
		this.monthlyTrends = monthlyTrends;
	}

	public InsightResponse(Map<String, BigDecimal> categoryWiseTotals, Map<String, BigDecimal> monthlyTrends) {
		super();
		this.categoryWiseTotals = categoryWiseTotals;
		this.monthlyTrends = monthlyTrends;
	}

	public InsightResponse() {
		super();
	}

}
