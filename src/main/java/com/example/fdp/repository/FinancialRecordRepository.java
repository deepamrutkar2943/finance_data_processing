package com.example.fdp.repository;

import com.example.fdp.model.FinancialRecord;
import com.example.fdp.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

	// Filter by transaction type (INCOME or EXPENSE)
	List<FinancialRecord> findByType(TransactionType type);

	// Filter by category
	List<FinancialRecord> findByCategory(String category);

	// Filter by date range
	List<FinancialRecord> findByDateBetween(LocalDate startDate, LocalDate endDate);

	// Filter by type and date range combined
	List<FinancialRecord> findByTypeAndDateBetween(TransactionType type, LocalDate startDate, LocalDate endDate);

	// Filter by category and date range combined
	List<FinancialRecord> findByCategoryAndDateBetween(String category, LocalDate startDate, LocalDate endDate);

	// Dashboard - total income
	@Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f WHERE f.type = 'INCOME'")
	BigDecimal calculateTotalIncome();

	// Dashboard - total expenses
	@Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f WHERE f.type = 'EXPENSE'")
	BigDecimal calculateTotalExpenses();

	// Dashboard - recent records, ordered by date
	List<FinancialRecord> findTop5ByOrderByDateDesc();

	// Insights - category wise totals
	@Query("SELECT f.category, SUM(f.amount) FROM FinancialRecord f GROUP BY f.category")
	List<Object[]> findCategoryWiseTotals();

	// Insights - monthly trends
	@Query(value = "SELECT DATE_FORMAT(date, '%Y-%m') as month, SUM(amount) as total FROM financial_records GROUP BY DATE_FORMAT(date, '%Y-%m') ORDER BY month ASC", nativeQuery = true)
	List<Object[]> findMonthlyTrends();

}
