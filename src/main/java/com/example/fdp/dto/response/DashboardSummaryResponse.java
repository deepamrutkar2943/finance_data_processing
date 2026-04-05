package com.example.fdp.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class DashboardSummaryResponse {

	private BigDecimal totalIncome;
	private BigDecimal totalExpenses;
	private BigDecimal netBalance;
	private List<RecordResponse> recentRecords;

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getTotalExpenses() {
		return totalExpenses;
	}

	public void setTotalExpenses(BigDecimal totalExpenses) {
		this.totalExpenses = totalExpenses;
	}

	public BigDecimal getNetBalance() {
		return netBalance;
	}

	public void setNetBalance(BigDecimal netBalance) {
		this.netBalance = netBalance;
	}

	public List<RecordResponse> getRecentRecords() {
		return recentRecords;
	}

	public void setRecentRecords(List<RecordResponse> recentRecords) {
		this.recentRecords = recentRecords;
	}

	public DashboardSummaryResponse(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal netBalance,
			List<RecordResponse> recentRecords) {
		super();
		this.totalIncome = totalIncome;
		this.totalExpenses = totalExpenses;
		this.netBalance = netBalance;
		this.recentRecords = recentRecords;
	}

	public DashboardSummaryResponse() {
		super();
	}

}
