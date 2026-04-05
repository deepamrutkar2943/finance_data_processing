package com.example.fdp.service;

import com.example.fdp.dto.response.DashboardSummaryResponse;
import com.example.fdp.dto.response.InsightResponse;
import com.example.fdp.dto.response.RecordResponse;
import com.example.fdp.mapper.FinancialRecordMapper;
import com.example.fdp.model.FinancialRecord;
import com.example.fdp.model.TransactionType;
import com.example.fdp.repository.FinancialRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceImplTest {

    @Mock
    private FinancialRecordRepository financialRecordRepository;

    @Mock
    private FinancialRecordMapper financialRecordMapper;

    @InjectMocks
    private DashboardServiceImpl dashboardServiceImpl;

    private FinancialRecord incomeRecord;
    private FinancialRecord expenseRecord;
    private RecordResponse incomeResponse;
    private RecordResponse expenseResponse;

    @BeforeEach
    void setUp() {
        incomeRecord = new FinancialRecord();
        incomeRecord.setId(1L);
        incomeRecord.setAmount(new BigDecimal("50000.00"));
        incomeRecord.setType(TransactionType.INCOME);
        incomeRecord.setCategory("Salary");
        incomeRecord.setDate(LocalDate.of(2024, 1, 1));

        expenseRecord = new FinancialRecord();
        expenseRecord.setId(2L);
        expenseRecord.setAmount(new BigDecimal("10000.00"));
        expenseRecord.setType(TransactionType.EXPENSE);
        expenseRecord.setCategory("Rent");
        expenseRecord.setDate(LocalDate.of(2024, 1, 5));

        incomeResponse = new RecordResponse();
        incomeResponse.setId(1L);
        incomeResponse.setAmount(new BigDecimal("50000.00"));
        incomeResponse.setType(TransactionType.INCOME);
        incomeResponse.setCategory("Salary");

        expenseResponse = new RecordResponse();
        expenseResponse.setId(2L);
        expenseResponse.setAmount(new BigDecimal("10000.00"));
        expenseResponse.setType(TransactionType.EXPENSE);
        expenseResponse.setCategory("Rent");
    }

    @Test
    void getSummary_Success() {
        when(financialRecordRepository.calculateTotalIncome())
                .thenReturn(new BigDecimal("50000.00"));
        when(financialRecordRepository.calculateTotalExpenses())
                .thenReturn(new BigDecimal("10000.00"));
        when(financialRecordRepository.findTop5ByOrderByDateDesc())
                .thenReturn(Arrays.asList(incomeRecord, expenseRecord));
        when(financialRecordMapper.toResponse(incomeRecord)).thenReturn(incomeResponse);
        when(financialRecordMapper.toResponse(expenseRecord)).thenReturn(expenseResponse);

        DashboardSummaryResponse response = dashboardServiceImpl.getSummary();

        assertNotNull(response);
        assertEquals(new BigDecimal("50000.00"), response.getTotalIncome());
        assertEquals(new BigDecimal("10000.00"), response.getTotalExpenses());
        assertEquals(new BigDecimal("40000.00"), response.getNetBalance());
        assertEquals(2, response.getRecentRecords().size());
    }

    @Test
    void getSummary_NoRecords_ReturnsZeros() {
        when(financialRecordRepository.calculateTotalIncome())
                .thenReturn(BigDecimal.ZERO);
        when(financialRecordRepository.calculateTotalExpenses())
                .thenReturn(BigDecimal.ZERO);
        when(financialRecordRepository.findTop5ByOrderByDateDesc())
                .thenReturn(Arrays.asList());

        DashboardSummaryResponse response = dashboardServiceImpl.getSummary();

        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.getTotalIncome());
        assertEquals(BigDecimal.ZERO, response.getTotalExpenses());
        assertEquals(BigDecimal.ZERO, response.getNetBalance());
        assertTrue(response.getRecentRecords().isEmpty());
    }

    @Test
    void getInsights_Success() {
        Object[] categoryRow1 = new Object[]{"Salary", new BigDecimal("50000.00")};
        Object[] categoryRow2 = new Object[]{"Rent", new BigDecimal("10000.00")};
        Object[] monthRow1 = new Object[]{"2024-01", new BigDecimal("60000.00")};

        List<Object[]> categoryData = new ArrayList<>();
        categoryData.add(categoryRow1);
        categoryData.add(categoryRow2);

        List<Object[]> monthlyData = new ArrayList<>();
        monthlyData.add(monthRow1);

        when(financialRecordRepository.findCategoryWiseTotals())
                .thenReturn(categoryData);
        when(financialRecordRepository.findMonthlyTrends())
                .thenReturn(monthlyData);

        InsightResponse response = dashboardServiceImpl.getInsights();

        assertNotNull(response);
        assertEquals(2, response.getCategoryWiseTotals().size());
        assertEquals(new BigDecimal("50000.00"), response.getCategoryWiseTotals().get("Salary"));
        assertEquals(new BigDecimal("10000.00"), response.getCategoryWiseTotals().get("Rent"));
        assertEquals(1, response.getMonthlyTrends().size());
        assertEquals(new BigDecimal("60000.00"), response.getMonthlyTrends().get("2024-01"));
    }

    @Test
    void getInsights_NoData_ReturnsEmptyMaps() {
        when(financialRecordRepository.findCategoryWiseTotals())
                .thenReturn(Arrays.asList());
        when(financialRecordRepository.findMonthlyTrends())
                .thenReturn(Arrays.asList());

        InsightResponse response = dashboardServiceImpl.getInsights();

        assertNotNull(response);
        assertTrue(response.getCategoryWiseTotals().isEmpty());
        assertTrue(response.getMonthlyTrends().isEmpty());
    }
}