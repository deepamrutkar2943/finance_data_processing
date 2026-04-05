package com.example.fdp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fdp.dto.request.CreateRecordRequest;
import com.example.fdp.dto.request.UpdateRecordRequest;
import com.example.fdp.dto.response.RecordResponse;
import com.example.fdp.model.TransactionType;
import com.example.fdp.service.FinancialRecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/records")
@Tag(name = "Financial Records", description = "Manage financial records")
public class FinancialRecordController {

	private final FinancialRecordService financialRecordService;

	public FinancialRecordController(FinancialRecordService financialRecordService) {
		super();
		this.financialRecordService = financialRecordService;
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Create record", description = "Admin only - create financial record")
	public ResponseEntity<RecordResponse> createRecord(@Valid @RequestBody CreateRecordRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(financialRecordService.createRecord(request));
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
	@Operation(summary = "Get all records", description = "All roles - get all financial records")
	public ResponseEntity<List<RecordResponse>> getAllRecords() {
		return ResponseEntity.ok(financialRecordService.getAllRecords());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
	@Operation(summary = "Get record by id", description = "All roles - get record by id")
	public ResponseEntity<RecordResponse> getRecordById(@PathVariable Long id) {
		return ResponseEntity.ok(financialRecordService.getRecordById(id));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Update record", description = "Admin only - update financial record")
	public ResponseEntity<RecordResponse> updateRecord(@PathVariable Long id,
			@Valid @RequestBody UpdateRecordRequest request) {
		return ResponseEntity.ok(financialRecordService.updateRecord(id, request));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Delete record", description = "Admin only - delete financial record")
	public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
		financialRecordService.deleteRecord(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/filter")
	@PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
	@Operation(summary = "Filter records", description = "All roles - filter records by type, category, date range")
	public ResponseEntity<List<RecordResponse>> filterRecords(@RequestParam(required = false) TransactionType type,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		return ResponseEntity.ok(financialRecordService.filterRecords(type, category, startDate, endDate));
	}
}