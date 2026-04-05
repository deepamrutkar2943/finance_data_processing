package com.example.fdp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.fdp.dto.request.CreateRecordRequest;
import com.example.fdp.dto.request.UpdateRecordRequest;
import com.example.fdp.dto.response.RecordResponse;
import com.example.fdp.exceptions.custom.RecordNotFoundException;
import com.example.fdp.exceptions.custom.UserNotFoundException;
import com.example.fdp.mapper.FinancialRecordMapper;
import com.example.fdp.model.FinancialRecord;
import com.example.fdp.model.TransactionType;
import com.example.fdp.model.User;
import com.example.fdp.repository.FinancialRecordRepository;
import com.example.fdp.repository.UserRepository;

@Service

public class FinancialRecordServiceImpl implements FinancialRecordService {

	private final FinancialRecordRepository financialRecordRepository;
	private final UserRepository userRepository;
	private final FinancialRecordMapper financialRecordMapper;

	public FinancialRecordServiceImpl(FinancialRecordRepository financialRecordRepository,
			UserRepository userRepository, FinancialRecordMapper financialRecordMapper) {
		super();
		this.financialRecordRepository = financialRecordRepository;
		this.userRepository = userRepository;
		this.financialRecordMapper = financialRecordMapper;
	}

	// Helper method to get currently logged in user
	private User getCurrentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("Logged in user not found"));
	}

	@Override
	public RecordResponse createRecord(CreateRecordRequest request) {
		User currentUser = getCurrentUser();

		FinancialRecord record = new FinancialRecord();
		record.setAmount(request.getAmount());
		record.setType(request.getType());
		record.setCategory(request.getCategory());
		record.setDate(request.getDate());
		record.setNotes(request.getNotes());
		record.setCreatedBy(currentUser);

		FinancialRecord saved = financialRecordRepository.save(record);
		return financialRecordMapper.toResponse(saved);
	}

	@Override
	public List<RecordResponse> getAllRecords() {
		return financialRecordRepository.findAll().stream().map(financialRecordMapper::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public RecordResponse getRecordById(Long id) {
		FinancialRecord record = financialRecordRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Record not found with id: " + id));
		return financialRecordMapper.toResponse(record);
	}

	@Override
	public RecordResponse updateRecord(Long id, UpdateRecordRequest request) {
		FinancialRecord record = financialRecordRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Record not found with id: " + id));

		record.setAmount(request.getAmount());
		record.setType(request.getType());
		record.setCategory(request.getCategory());
		record.setDate(request.getDate());
		record.setNotes(request.getNotes());

		FinancialRecord updated = financialRecordRepository.save(record);
		return financialRecordMapper.toResponse(updated);
	}

	@Override
	public void deleteRecord(Long id) {
		FinancialRecord record = financialRecordRepository.findById(id)
				.orElseThrow(() -> new RecordNotFoundException("Record not found with id: " + id));
		financialRecordRepository.delete(record);
	}

	@Override
	public List<RecordResponse> filterRecords(TransactionType type, String category, LocalDate startDate,
			LocalDate endDate) {
		// All filters provided
		if (type != null && category != null && startDate != null && endDate != null) {
			return financialRecordRepository.findByTypeAndDateBetween(type, startDate, endDate).stream()
					.filter(r -> r.getCategory().equalsIgnoreCase(category)).map(financialRecordMapper::toResponse)
					.collect(Collectors.toList());
		}
		// Type and date range
		if (type != null && startDate != null && endDate != null) {
			return financialRecordRepository.findByTypeAndDateBetween(type, startDate, endDate).stream()
					.map(financialRecordMapper::toResponse).collect(Collectors.toList());
		}
		// Category and date range
		if (category != null && startDate != null && endDate != null) {
			return financialRecordRepository.findByCategoryAndDateBetween(category, startDate, endDate).stream()
					.map(financialRecordMapper::toResponse).collect(Collectors.toList());
		}
		// Type only
		if (type != null) {
			return financialRecordRepository.findByType(type).stream().map(financialRecordMapper::toResponse)
					.collect(Collectors.toList());
		}
		// Category only
		if (category != null) {
			return financialRecordRepository.findByCategory(category).stream().map(financialRecordMapper::toResponse)
					.collect(Collectors.toList());
		}
		// Date range only
		if (startDate != null && endDate != null) {
			return financialRecordRepository.findByDateBetween(startDate, endDate).stream()
					.map(financialRecordMapper::toResponse).collect(Collectors.toList());
		}
		// No filters - return all
		return getAllRecords();
	}
}