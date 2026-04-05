package com.example.fdp.service;

import com.example.fdp.dto.request.CreateRecordRequest;
import com.example.fdp.dto.request.UpdateRecordRequest;
import com.example.fdp.dto.response.RecordResponse;
import com.example.fdp.model.TransactionType;

import java.time.LocalDate;
import java.util.List;

public interface FinancialRecordService {
    RecordResponse createRecord(CreateRecordRequest request);
    List<RecordResponse> getAllRecords();
    RecordResponse getRecordById(Long id);
    RecordResponse updateRecord(Long id, UpdateRecordRequest request);
    void deleteRecord(Long id);
    List<RecordResponse> filterRecords(TransactionType type, String category, LocalDate startDate, LocalDate endDate);
}