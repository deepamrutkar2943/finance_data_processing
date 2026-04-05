package com.example.fdp.service;

import com.example.fdp.dto.request.CreateRecordRequest;
import com.example.fdp.dto.request.UpdateRecordRequest;
import com.example.fdp.dto.response.RecordResponse;
import com.example.fdp.exceptions.custom.RecordNotFoundException;
import com.example.fdp.mapper.FinancialRecordMapper;
import com.example.fdp.model.FinancialRecord;
import com.example.fdp.model.Role;
import com.example.fdp.model.TransactionType;
import com.example.fdp.model.User;
import com.example.fdp.model.UserStatus;
import com.example.fdp.repository.FinancialRecordRepository;
import com.example.fdp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinancialRecordServiceImplTest {

    @Mock
    private FinancialRecordRepository financialRecordRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FinancialRecordMapper financialRecordMapper;

    @InjectMocks
    private FinancialRecordServiceImpl financialRecordServiceImpl;

    private User user;
    private FinancialRecord record;
    private RecordResponse recordResponse;
    private CreateRecordRequest createRecordRequest;
    private UpdateRecordRequest updateRecordRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Admin User");
        user.setEmail("admin@gmail.com");
        user.setRole(Role.ADMIN);
        user.setStatus(UserStatus.ACTIVE);

        record = new FinancialRecord();
        record.setId(1L);
        record.setAmount(new BigDecimal("50000.00"));
        record.setType(TransactionType.INCOME);
        record.setCategory("Salary");
        record.setDate(LocalDate.of(2024, 1, 1));
        record.setNotes("Monthly salary");
        record.setCreatedBy(user);

        recordResponse = new RecordResponse();
        recordResponse.setId(1L);
        recordResponse.setAmount(new BigDecimal("50000.00"));
        recordResponse.setType(TransactionType.INCOME);
        recordResponse.setCategory("Salary");
        recordResponse.setDate(LocalDate.of(2024, 1, 1));
        recordResponse.setNotes("Monthly salary");
        recordResponse.setCreatedByName("Admin User");

        createRecordRequest = new CreateRecordRequest();
        createRecordRequest.setAmount(new BigDecimal("50000.00"));
        createRecordRequest.setType(TransactionType.INCOME);
        createRecordRequest.setCategory("Salary");
        createRecordRequest.setDate(LocalDate.of(2024, 1, 1));
        createRecordRequest.setNotes("Monthly salary");

        updateRecordRequest = new UpdateRecordRequest();
        updateRecordRequest.setAmount(new BigDecimal("60000.00"));
        updateRecordRequest.setType(TransactionType.INCOME);
        updateRecordRequest.setCategory("Salary");
        updateRecordRequest.setDate(LocalDate.of(2024, 1, 1));
        updateRecordRequest.setNotes("Updated salary");

    }

    @Test
    void createRecord_Success() {
        // Mock SecurityContext (MOVED HERE)
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("admin@gmail.com");
        SecurityContextHolder.setContext(securityContext);

        // Existing test code...
        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(user));
        when(financialRecordRepository.save(any(FinancialRecord.class))).thenReturn(record);
        when(financialRecordMapper.toResponse(record)).thenReturn(recordResponse);

        RecordResponse response = financialRecordServiceImpl.createRecord(createRecordRequest);

        // ... assertions
    }

    @Test
    void getAllRecords_Success() {
        when(financialRecordRepository.findAll()).thenReturn(Arrays.asList(record));
        when(financialRecordMapper.toResponse(record)).thenReturn(recordResponse);

        List<RecordResponse> responses = financialRecordServiceImpl.getAllRecords();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Salary", responses.get(0).getCategory());
    }

    @Test
    void getRecordById_Success() {
        when(financialRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(financialRecordMapper.toResponse(record)).thenReturn(recordResponse);

        RecordResponse response = financialRecordServiceImpl.getRecordById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getRecordById_NotFound_ThrowsException() {
        when(financialRecordRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> financialRecordServiceImpl.getRecordById(99L));
    }

    @Test
    void updateRecord_Success() {
        recordResponse.setAmount(new BigDecimal("60000.00"));

        when(financialRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(financialRecordRepository.save(any(FinancialRecord.class))).thenReturn(record);
        when(financialRecordMapper.toResponse(record)).thenReturn(recordResponse);

        RecordResponse response = financialRecordServiceImpl.updateRecord(1L, updateRecordRequest);

        assertNotNull(response);
        assertEquals(new BigDecimal("60000.00"), response.getAmount());
        verify(financialRecordRepository, times(1)).save(record);
    }

    @Test
    void updateRecord_NotFound_ThrowsException() {
        when(financialRecordRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class,
                () -> financialRecordServiceImpl.updateRecord(99L, updateRecordRequest));
    }

    @Test
    void deleteRecord_Success() {
        when(financialRecordRepository.findById(1L)).thenReturn(Optional.of(record));

        financialRecordServiceImpl.deleteRecord(1L);

        verify(financialRecordRepository, times(1)).delete(record);
    }

    @Test
    void deleteRecord_NotFound_ThrowsException() {
        when(financialRecordRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> financialRecordServiceImpl.deleteRecord(99L));
    }

    @Test
    void filterRecords_ByType_Success() {
        when(financialRecordRepository.findByType(TransactionType.INCOME))
                .thenReturn(Arrays.asList(record));
        when(financialRecordMapper.toResponse(record)).thenReturn(recordResponse);

        List<RecordResponse> responses = financialRecordServiceImpl
                .filterRecords(TransactionType.INCOME, null, null, null);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(TransactionType.INCOME, responses.get(0).getType());
    }

    @Test
    void filterRecords_ByCategory_Success() {
        when(financialRecordRepository.findByCategory("Salary"))
                .thenReturn(Arrays.asList(record));
        when(financialRecordMapper.toResponse(record)).thenReturn(recordResponse);

        List<RecordResponse> responses = financialRecordServiceImpl
                .filterRecords(null, "Salary", null, null);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Salary", responses.get(0).getCategory());
    }

    @Test
    void filterRecords_ByDateRange_Success() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        when(financialRecordRepository.findByDateBetween(start, end))
                .thenReturn(Arrays.asList(record));
        when(financialRecordMapper.toResponse(record)).thenReturn(recordResponse);

        List<RecordResponse> responses = financialRecordServiceImpl
                .filterRecords(null, null, start, end);

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }
}