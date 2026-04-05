package com.example.fdp.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.fdp.dto.response.DashboardSummaryResponse;
import com.example.fdp.dto.response.InsightResponse;
import com.example.fdp.dto.response.RecordResponse;
import com.example.fdp.mapper.FinancialRecordMapper;
import com.example.fdp.repository.FinancialRecordRepository;

@Service
public class DashboardServiceImpl implements DashboardService {

	private final FinancialRecordRepository financialRecordRepository;
	private final FinancialRecordMapper financialRecordMapper;

	public DashboardServiceImpl(FinancialRecordRepository financialRecordRepository,
			FinancialRecordMapper financialRecordMapper) {
		super();
		this.financialRecordRepository = financialRecordRepository;
		this.financialRecordMapper = financialRecordMapper;
	}

	@Override
	public DashboardSummaryResponse getSummary() {
		BigDecimal totalIncome   = financialRecordRepository.calculateTotalIncome();
		BigDecimal totalExpenses = financialRecordRepository.calculateTotalExpenses();

		// Guard against null from Hibernate when table is empty
		if (totalIncome   == null) totalIncome   = BigDecimal.ZERO;
		if (totalExpenses == null) totalExpenses = BigDecimal.ZERO;

		BigDecimal netBalance = totalIncome.subtract(totalExpenses);


		List<RecordResponse> recentRecords = financialRecordRepository.findTop5ByOrderByDateDesc().stream()
				.map(financialRecordMapper::toResponse).collect(Collectors.toList());

		DashboardSummaryResponse summary = new DashboardSummaryResponse();
		summary.setTotalIncome(totalIncome);
		summary.setTotalExpenses(totalExpenses);
		summary.setNetBalance(netBalance);
		summary.setRecentRecords(recentRecords);
		return summary;
	}

	@Override
	public InsightResponse getInsights() {

		// Convert Object[] results into Map<String, BigDecimal>
		Map<String, BigDecimal> categoryWiseTotals = new LinkedHashMap<>();
		financialRecordRepository.findCategoryWiseTotals()
				.forEach(row -> categoryWiseTotals.put((String) row[0], (BigDecimal) row[1]));

		Map<String, BigDecimal> monthlyTrends = new LinkedHashMap<>();
		financialRecordRepository.findMonthlyTrends()
				.forEach(row -> monthlyTrends.put((String) row[0], (BigDecimal) row[1]));

		InsightResponse insight = new InsightResponse();
		insight.setCategoryWiseTotals(categoryWiseTotals);
		insight.setMonthlyTrends(monthlyTrends);
		return insight;
	}
}